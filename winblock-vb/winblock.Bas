Attribute VB_Name = "WinBlockGlob"
Public blocksAcross As Integer     ' The number of blocks across
Public blocksDown As Integer       ' The number of blocks down

Public paddlex As Single          ' The current paddle position
Public paddley As Single          ' The paddle's y-position (fixed)
Public ballx, bally As Single       '  The ball's x, y position
Public rballx, rbally As Single     ' The ball's icon's x,y position
Dim ballstepx, ballstepy As Single  ' The current motion of the ball per timer interval
Public ballspeed As Integer            ' The speed of the ball

Public ismovable As Boolean        ' Flag to indicate it is OK to move the paddle
Public ballismoving As Boolean     ' flag to indicate if the ball is on the move (has been released)

Type block                         ' data structure for our blocks
    isAlive   As Boolean           ' Whether the block is visible
    pointsVal As Integer           ' The points value of the block
    hitsNeeded As Integer          ' Hits required before the block is destroyed.
                                    ' Can be negative => the block cannot be destroyed.
    X As Single                     ' Makes life a little easier (calculations not required)
    Y As Single
End Type


Dim theBlocks() As block

Const LEFTINDENT = 8            ' The number of pixels to indent on the left
Const TOPINDENT = 20            ' The number of pixels to indent at the top
Const BLOCKXSPACE = 2           ' Pixels between each block horizontally
Const BLOCKYSPACE = 2           ' Pixels between each block vertically
Const PADDLERAISED = 20 ' Height from the bottom of the game area of the paddle
Const PADDLESTEP = 20    ' Pixels to jump (speed of paddle)

Const BALLINDENT = 12   ' The indentation in the icon structure
Const BALLWIDTH = 8    ' The width of the ball in pixels

' DLL imports

Declare Function sndPlaySound Lib "WINMM.DLL" Alias "sndPlaySoundA" _
      (ByVal lpszSoundName As String, ByVal uFlags As Long) As Long

   Declare Function RegCreateKey& Lib "SHELL.DLL" (ByVal hKey&, ByVal lpszSubKey$, lphKey&)

   Declare Function RegSetValue& Lib "SHELL.DLL" (ByVal hKey&, ByVal lpszSubKey$, ByVal fdwType&, ByVal lpszValue$, ByVal dwLength&)

   ' Return codes from Registration functions.
   Const ERROR_SUCCESS = 0&
   Const ERROR_BADDB = 1&
   Const ERROR_BADKEY = 2&
   Const ERROR_CANTOPEN = 3&
   Const ERROR_CANTREAD = 4&

   Const ERROR_CANTWRITE = 5&
   Const ERROR_OUTOFMEMORY = 6&
   Const ERROR_INVALID_PARAMETER = 7&
   Const ERROR_ACCESS_DENIED = 8&

   Const HKEY_CLASSES_ROOT = 1
   Const HKEY_LOCAL_MACHINE = 3
   Const MAX_PATH = 256&
   Const REG_SZ = 1

Dim Plives, Pscore As Integer       ' Player's lives and score
Const STARTLIVES = 3

' High score table

Public names(5) As String
Public scores(5) As String

Public playsounds As Boolean

Public ballSprite As Integer

Public lockMouse As Boolean

Public Const BLUE = 1
Public Const CYAN = 2
Public Const GREEN = 3
Public Const GREY = 4
Public Const RED = 5
Public Const YELLOW = 6
Public Const BLACK = 7

' API Calls

Type RECT
    left As Long
    top As Long
    right As Long
    bottom As Long
End Type

Declare Function ClipCursor Lib "User32.dll" (ByRef lprect As RECT) As Boolean
Declare Function GetSystemMetrics Lib "User32.dll" (ByVal nIndex As Integer) As Integer

Const SM_CYMIN = 29
Const SM_CYMENUSIZE = 55



Function ballIsTouching(someblock As block) As Boolean
' Determines if the ball is touching the given block

Dim ballright, ballleft, balltop, ballbottom As Double
Dim blockright, blockleft, blocktop, blockbottom As Double

If someblock.isAlive Then


    ballright = ballx + BALLWIDTH
    ballleft = ballx
    balltop = bally
    ballbottom = bally + BALLWIDTH
    blockright = someblock.X + frmG.BlockImages.ImageWidth
    blockleft = someblock.X
    blocktop = someblock.Y
    blockbottom = someblock.Y + frmG.BlockImages.ImageHeight
    
 '   If someblock.x = theBlocks(2, 4).x And someblock.y = theBlocks(2, 4).y Then
    
 '   Debug.Print "First condition: ballright >= blockleft: "; ballright; ">="; blockleft
 '   Debug.Print "Second condition: ballleft <= blockright: "; ballleft; "<="; blockright
 '   Debug.Print "Third condition: ballbottom >= blocktop: "; ballbottom; ">="; blocktop
 '   Debug.Print "Fourth condition: balltop <= blockbottom: "; balltop; "<="; blockbottom
  '  End If
    If ballright >= blockleft And ballleft <= blockright And ballbottom >= blocktop And balltop <= blockbottom Then
    ballIsTouching = True
    Else
    ballIsTouching = False
    End If
    
Else
ballIsTouching = False

End If

    

End Function

Sub centreball()
' centres the ball on the paddle
    setBallXY paddlex + Int(0.5 * frmG.LongSprites.ImageWidth) - Int(0.5 * BALLWIDTH), paddley - BALLWIDTH
    drawball
End Sub

Sub checkballbounds()
' Check to see if the ball has collided with an object (or the edge of the screen)
' If the ball has collided, set it's position to be just touching the object,
' and change the ballstepx and ballstepy to produce bounce.

' first, check for a collision with the paddle

Dim i, j, hBlock As Integer
Dim layBott, layTop As Single
Dim colLeft, colRight As Single
Dim ballleft, ballright As Single
Dim ballcentre As Single
Dim paddlecentre As Single
Dim balldiv As Single


frmG.balltimer.Enabled = False                    ' In case these calculations take longer than the timer interval

If (bally + BALLWIDTH) >= paddley Then        ' if the bottom of the ball is in or touching the paddle
    If ballx + BALLWIDTH >= paddlex And ballx <= paddlex + frmG.LongSprites.ImageWidth Then

            ballcentre = ballx + Int(0.5 * BALLWIDTH)
            paddlecentre = paddlex + Int(0.5 * frmG.LongSprites.ImageWidth)
            balldiv = Int(0.090909 * frmG.LongSprites.ImageWidth)        ' 11 divisions
        If ballcentre > paddlecentre + 4 * balldiv Then
            ' ball is in rightmost division of paddle
            ballstepx = 3
        ElseIf ballcentre > paddlecentre + 3 * balldiv Then
            ballstepx = 2
        ElseIf ballcentre > paddlecentre + 2 * balldiv Then
            ballstepx = 1.5
        ElseIf ballcentre > paddlecentre + balldiv Then
            ballstepx = 1
        ElseIf ballcentre > paddlecentre Then
            ballstepx = 0
        ElseIf ballcentre > paddlecentre - balldiv Then
            ballstepx = -1
        ElseIf ballcentre > paddlecentre - 2 * balldiv Then
            ballstepx = -1.5
        ElseIf ballcentre > paddlecentre - 3 * balldiv Then
            ballstepx = -2
        Else
            ballstepx = -3
        End If
        ballstepy = -ballstepy
        setBallXY ballx, paddley - BALLWIDTH
        frmG.GameArea.PaintPicture frmG.LongSprites.ListImages("pnormal").Picture, paddlex, paddley
                 doPop
    End If
End If

' check for screen boundaries

If ballx <= frmG.GameArea.ScaleLeft Then    ' Left edge of the gamearea
    doPop
    setBallXY frmG.GameArea.ScaleLeft, bally    ' Stop at the left edge
    ballstepx = -ballstepx                  ' Reverse the ball's vertical direction
End If

If (ballx + BALLWIDTH) >= (frmG.GameArea.ScaleLeft + frmG.GameArea.ScaleWidth) Then ' Right edge
    doPop
    setBallXY (frmG.GameArea.ScaleLeft + frmG.GameArea.ScaleWidth - BALLWIDTH), bally
    ballstepx = -ballstepx
End If

If bally <= frmG.GameArea.ScaleTop Then     ' Top of the gamearea
    doPop
    setBallXY ballx, frmG.GameArea.ScaleTop     ' stop at the top
    ballstepy = -ballstepy                  ' Reverse direction
End If


If bally >= (paddley + frmG.LongSprites.ImageHeight) Then ' Ball is past saving at the bottom
    lostball
End If

' now check for contact with blocks (at the top or bottom)

For i = blocksDown To 1 Step -1
For j = 1 To blocksAcross
    If ballIsTouching(theBlocks(j, i)) Then
        blastblock j, i ' Destruction!!
        doBlock                                     ' Play the sound (if sounds are switched on)
        ballstepy = -ballstepy                          'Reverse vertical step (bounce)
        ballstepx = -ballstepx                          ' Reverse horizontal step
      '  If bally <= layBott And bally >= layTop Then
      '      setBallXY ballx, theBlocks(hBlock, i).Y + frmG.BlockImages.ImageHeight
      '  Else
      '      setBallXY ballx, theBlocks(hBlock, i).Y - frmG.BlockImages.ImageHeight - BALLWIDTH
        'End If
    End If
    
Next j
Next i



'For i = blocksDown To 1 Step -1
'    layBott = theBlocks(1, i).Y + frmG.BlockImages.ImageHeight
'    layTop = theBlocks(1, i).Y
'    If (bally <= layBott And bally >= layTop) Or (bally + BALLWIDTH >= layTop And bally + BALLWIDTH <= layBott) Then   ' Hit this layer vertically
'        hBlock = xblockat(ballx)
'        If theBlocks(hBlock, i).isAlive And hBlock > 0 Then
'            blastblock hBlock, i ' Destruction!!
'            doBlock
'            ballstepy = -ballstepy                          'Reverse vertical step (bounce)
'          '  RedrawBlocks
'            If bally <= layBott And bally >= layTop Then
'              setBallXY ballx, theBlocks(hBlock, i).Y + frmG.BlockImages.ImageHeight
'            Else
'             setBallXY ballx, theBlocks(hBlock, i).Y - frmG.BlockImages.ImageHeight - BALLWIDTH
'            End If
'
          ' Exit For
'        End If
'    End If
'Next i
    
' check for striking the edges of a box

'For i = 1 To blocksAcross
'    colLeft = theBlocks(i, 1).X
'    colRight = theBlocks(i, 1).X + frmG.BlockImages.ImageWidth
'    ballleft = ballx                ' left edge of ball
'    ballright = ballx + BALLWIDTH   ' right edge of ball
''    ' right edge of ball >= left edge of block and <= right edge of block OR
'    ' left edge of ball <= right edge of block and >= left edge of block
'    If (ballright >= colLeft And ballright <= colRight) Or (ballleft <= colRight And ballleft >= colLeft) Then
'        vblock = yblockat(bally)
'        If theBlocks(i, vblock).isAlive And vblock > 0 Then
'            blastblock i, vblock
'            doBlock
        '    RedrawBlocks
'            ballstepx = -ballstepx
            'Exit For

'        End If
'    End If
'Next i


frmG.balltimer.Enabled = True


End Sub

Sub blastblock(ByVal xpos As Integer, ByVal ypos As Integer)
    theBlocks(xpos, ypos).hitsNeeded = theBlocks(xpos, ypos).hitsNeeded - 1 ' possible bug for non-destructable blocks.
    If theBlocks(xpos, ypos).hitsNeeded = 0 Then        ' The block should be terminated
        theBlocks(xpos, ypos).isAlive = False
        frmG.GameArea.Line (theBlocks(xpos, ypos).X, theBlocks(xpos, ypos).Y)-(theBlocks(xpos, ypos).X + frmG.BlockImages.ImageWidth, theBlocks(xpos, ypos).Y + frmG.BlockImages.ImageHeight), frmG.GameArea.BackColor, BF
        Pscore = Pscore + theBlocks(xpos, ypos).pointsVal * ballspeed
        frmG.score.Caption = Str$(Pscore)
        If finishedLevel Then ResetAll
    End If
End Sub


Sub ClipCurs()
    Dim ourrectangle As RECT
    Dim errorprmt As String
    Dim wintop As Integer
    
    wintop = GetSystemMetrics(SM_CYMIN) + GetSystemMetrics(SM_CYMENUSIZE)
    errorprmt = "The cursor could not be confined to the game area."
    errorprmt = errorprmt + Chr$(13) + "Resetting your control options for pointer clipping."
    If lockMouse Then
        ourrectangle.left = Int(frmG.left / Screen.TwipsPerPixelX) + 10
        ourrectangle.top = Int(frmG.top / Screen.TwipsPerPixelY) + wintop
        ourrectangle.right = Int(frmG.left / Screen.TwipsPerPixelX) + Int(frmG.Width / Screen.TwipsPerPixelX) - 10
        ourrectangle.bottom = Int(frmG.top / Screen.TwipsPerPixelY) + Int(frmG.Height / Screen.TwipsPerPixelY) - frmG.botmarker.Height

    dum = ClipCursor(ourrectangle)
      '      MsgBox errorprmpt, vbExclamation + vbOKOnly, "Internal Error"
      '  End If
    End If
End Sub


Sub doBlock()
    Dim thesound As String
    Dim soundf As Long
    If playsounds Then
    thesound = App.Path + "\sounds\block.wav"
    soundf = 1
    dum = sndPlaySound(thesound, soundf)
    End If
    
End Sub

Sub doError(isfatal As Boolean, message As String)
    MsgBox message
    If isfatal Then End
End Sub

Sub doPop()
    Dim thesound As String
    Dim soundf As Long
    If playsounds Then
    thesound = App.Path + "\sounds\pop.wav"
    soundf = &H1
    dum = sndPlaySound(thesound, soundf)
    End If
    
End Sub


Sub drawball()
If ismovable Then
    frmG.GameArea.PaintPicture frmG.SmallSprites.ListImages(ballSprite).Picture, rballx, rbally
End If

End Sub

Function finishedLevel() As Boolean
' We are only finished the level when all the (destroyable) blocks are dead.
Dim i, j As Integer
Dim reallyFinished As Boolean

    reallyFinished = True       ' Assume we are finished, until proven otherwise

    If ismovable Then           ' No point doing this if we have not initialised
    For i = 1 To blocksAcross
        For j = 1 To blocksDown
            If theBlocks(i, j).isAlive And theBlocks(i, j).hitsNeeded > 0 Then
                reallyFinished = False
                Exit For
            End If
        Next j
    Next i
    End If
    
    finishedLevel = reallyFinished

End Function

Sub GetRegSettings()
' gets the current preferences from the registry. Sets the defaults
' if a registry key does not exist.

If GetSetting(App.ProductName, "UserPrefs", "RegHere", "NO") = "NO" Then
    SetRegDefaults
Else

If GetSetting(App.ProductName, "UserPrefs", "Sounds", "1") = "1" Then
    playsounds = True
Else
    playsounds = False
End If

ballSprite = Val(GetSetting(App.ProductName, "UserPrefs", "BallSprite", "1"))

If GetSetting(App.ProductName, "UserPrefs", "LockMouse", "0") = "0" Then
    lockMouse = False
Else
    lockMouse = True
End If

ballspeed = Val(GetSetting(App.ProductName, "UserPrefs", "BallSpeed", "10"))

names(1) = GetSetting(App.ProductName, "Scores", "Name1", "Brian Duff")
scores(1) = GetSetting(App.ProductName, "Scores", "Score1", "100")
names(2) = GetSetting(App.ProductName, "Scores", "Name2", "Brian Duff")
scores(2) = GetSetting(App.ProductName, "Scores", "Score2", "80")
names(3) = GetSetting(App.ProductName, "Scores", "Name3", "Brian Duff")
scores(3) = GetSetting(App.ProductName, "Scores", "Score3", "75")
names(4) = GetSetting(App.ProductName, "Scores", "Name4", "Brian Duff")
scores(4) = GetSetting(App.ProductName, "Scores", "Score4", "50")
names(5) = GetSetting(App.ProductName, "Scores", "Name5", "Brian Duff")
scores(5) = GetSetting(App.ProductName, "Scores", "Score5", "25")

End If
End Sub

Sub RedrawBlocks()
Dim i, j As Integer

    For i = 1 To blocksAcross
        For j = 1 To blocksDown
            If theBlocks(i, j).isAlive Then
                frmG.GameArea.PaintPicture frmG.BlockImages.ListImages(j).Picture, theBlocks(i, j).X, theBlocks(i, j).Y
            End If
        Next j
    Next i
    
End Sub

Sub SetRegDefaults()
    SaveSetting App.ProductName, "UserPrefs", "RegHere", "YES"
    SaveSetting App.ProductName, "UserPrefs", "Sounds", "1"
    SaveSetting App.ProductName, "UserPrefs", "BallSprite", "1"
    SaveSetting App.ProductName, "UserPrefs", "BallSpeed", "10"
    SaveSetting App.ProductName, "Scores", "Name1", "Brian Duff"
    SaveSetting App.ProductName, "Scores", "Score1", "100"
    SaveSetting App.ProductName, "Scores", "Name2", "Brian Duff"
    SaveSetting App.ProductName, "Scores", "Score2", "80"
    SaveSetting App.ProductName, "Scores", "Name3", "Brian Duff"
    SaveSetting App.ProductName, "Scores", "Score3", "75"
    SaveSetting App.ProductName, "Scores", "Name4", "Brian Duff"
    SaveSetting App.ProductName, "Scores", "Score4", "50"
    SaveSetting App.ProductName, "Scores", "Name5", "Brian Duff"
    SaveSetting App.ProductName, "Scores", "Score5", "25"
End Sub

Sub SetRegSettings()
    Const t = "1"
    Const f = "0"
    Dim which As String
    
    
    SaveSetting App.ProductName, "UserPrefs", "RegHere", "YES"
    If playsounds Then which = t Else which = f
    SaveSetting App.ProductName, "UserPrefs", "Sounds", which
    SaveSetting App.ProductName, "UserPrefs", "BallSprite", Str$(ballSprite)
    SaveSetting App.ProductName, "UserPrefs", "BallSpeed", Str$(ballspeed)
    If lockMouse Then which = t Else which = f
    SaveSetting App.ProductName, "UserPrefs", "LockMouse", which
    SaveSetting App.ProductName, "Scores", "Name1", names(1)
    SaveSetting App.ProductName, "Scores", "Score1", scores(1)
    SaveSetting App.ProductName, "Scores", "Name2", names(2)
    SaveSetting App.ProductName, "Scores", "Score2", scores(2)
    SaveSetting App.ProductName, "Scores", "Name3", names(3)
    SaveSetting App.ProductName, "Scores", "Score3", scores(3)
    SaveSetting App.ProductName, "Scores", "Name4", names(4)
    SaveSetting App.ProductName, "Scores", "Score4", scores(4)
    SaveSetting App.ProductName, "Scores", "Name5", names(5)
    SaveSetting App.ProductName, "Scores", "Score5", scores(5)
    
End Sub

Sub initBall()
' initialise the ball on the centre of the paddle
' paddle must already be initialised
    
    centreball
    ballismoving = False
    

End Sub

Sub initBlocks()
    Dim blockWidth, blockHeight, i, j As Integer
    Dim curx, cury As Single
    
    ' No absolute measurements - block's graphic can change later
    blockWidth = frmG.BlockImages.ImageWidth
    blockHeight = frmG.BlockImages.ImageHeight
    blocksAcross = Int((frmG.GameArea.ScaleWidth - LEFTINDENT) / (blockWidth + BLOCKXSPACE))
    
    ' for now, just display a row of each colour.
    blocksDown = frmG.BlockImages.ListImages.Count
    If blocksDown > Int((frmG.GameArea.ScaleHeight - TOPINDENT) / (blockHeight + BLOCKYSPACE)) Then
        doError True, "INTERNAL ERROR: game area is too small!"
    End If
    
    ReDim theBlocks(blocksAcross, blocksDown)    ' Redimension the global array
    
    ' draw the blocks on the gamearea
    
    cury = TOPINDENT
    
    For i = 1 To blocksDown
        curx = LEFTINDENT
        For j = 1 To blocksAcross
            theBlocks(j, i).X = curx
            theBlocks(j, i).Y = cury
            frmG.GameArea.PaintPicture frmG.BlockImages.ListImages(i).Picture, curx, cury
            curx = curx + frmG.BlockImages.ImageWidth + BLOCKXSPACE
            theBlocks(j, i).hitsNeeded = 1
            theBlocks(j, i).isAlive = True
            theBlocks(j, i).pointsVal = 10

        Next j
        cury = cury + frmG.BlockImages.ImageHeight + BLOCKYSPACE
    Next i
            
    
    
End Sub


Sub initGame()
    initPaddle
    Pscore = 0
    Plives = STARTLIVES
'    frmG.score.ForeColor = &HFF00
'    frmG.lives.ForeColor = &HFF00
    frmG.score.Caption = Str$(Pscore)
    frmG.lives.Caption = Str$(Plives)

    initBlocks
    initBall
    ismovable = True
    ClipCurs
End Sub

Sub lostball()
' do nothing yet
Dim thesound As String
Dim soundf As Long
thesound = App.Path + "\sounds\failure.wav"
soundf = 1
If playsounds Then
dum = sndPlaySound(thesound, soundf)
End If

Plives = Plives - 1

frmG.lives.Caption = Str$(Plives)
initBall

If Plives = 0 Then ResetAll
End Sub

Sub releaseball()
    
    ballstepx = 0
    While ballstepx = 0
        ballstepx = Int(Rnd * 3) - 1        ' -1, 0 or 1 (random)
    Wend
    ballstepy = -1                       ' Up
    ballismoving = True                 ' Prevent the paddle from moving the ball
    frmG.balltimer.Enabled = True            ' Begin automatic movement


End Sub

Sub initPaddle()
        
    paddlex = Int(frmG.GameArea.ScaleWidth / 2) - Int(frmG.LongSprites.ImageWidth / 2)
    paddley = frmG.GameArea.ScaleHeight - PADDLERAISED - frmG.LongSprites.ImageHeight
    
    frmG.GameArea.PaintPicture frmG.LongSprites.ListImages("pnormal").Picture, paddlex, paddley
    
End Sub


Sub jumppaddle(tox As Single)
    If tox < frmG.GameArea.ScaleWidth - frmG.LongSprites.ImageWidth And tox >= 0 Then
    frmG.GameArea.Line (paddlex, paddley)-(paddlex + frmG.LongSprites.ImageWidth, paddley + frmG.LongSprites.ImageHeight), frmG.GameArea.BackColor, BF
    paddlex = tox
    frmG.GameArea.PaintPicture frmG.LongSprites.ListImages("pnormal").Picture, paddlex, paddley
    End If
    
End Sub

Sub moveball()
' Move the ball ballstepx*ballspeed, ballstepy*ballspeed
    setBallXY ballx + (ballstepx * ballspeed), bally + (ballstepy * ballspeed)
    checkballbounds
    drawball
End Sub

Sub movepaddle(direction As Integer, Shift As Integer)
    ' -1 is left, 1 is right
    
    Dim brx1, bry1, brx2, bry2 As Single    'Blanking rect
    Dim thepaddlestep As Integer
    
    If Shift = 0 Then
        thepaddlestep = PADDLESTEP
    Else
        thepaddlestep = frmG.LongSprites.ImageWidth
    End If
    If direction = -1 Then
        If paddlex - thepaddlestep > LEFTINDENT Then
            bry1 = paddley
            bry2 = paddley + frmG.LongSprites.ImageHeight
            brx1 = paddlex + thepaddlestep
            brx2 = paddlex + frmG.LongSprites.ImageWidth
    
            frmG.GameArea.Line (brx1, bry1)-(brx2, bry2), frmG.GameArea.BackColor, BF   ' Draw a filled box
            paddlex = paddlex - thepaddlestep
            frmG.GameArea.PaintPicture frmG.LongSprites.ListImages("pnormal").Picture, paddlex, paddley
    
        End If
    Else
        If paddlex + thepaddlestep < (frmG.GameArea.ScaleWidth - LEFTINDENT) Then
            bry1 = paddley
            bry2 = paddley + frmG.LongSprites.ImageHeight
            brx1 = paddlex
            brx2 = paddlex + thepaddlestep
    
            frmG.GameArea.Line (brx1, bry1)-(brx2, bry2), frmG.GameArea.BackColor, BF   ' Draw a filled box
            paddlex = paddlex + thepaddlestep
            frmG.GameArea.PaintPicture frmG.LongSprites.ListImages("pnormal").Picture, paddlex, paddley
        End If
    End If
    
End Sub


Sub ResetAll()
    ismovable = False
    ballismoving = False
    frmG.balltimer.Enabled = False
    frmG.GameArea.Cls
    frmG.mnuGamePause.Enabled = False
    frmG.mnuGameNewGame.Enabled = True
    frmG.lives.Caption = "0"
    frmG.score.Caption = "0"
    UnclipCurs
End Sub

Sub setBallXY(ByVal X As Single, ByVal Y As Single)
' Set the ball's top left hand point as x,y (offsetting for the icon)
    frmG.GameArea.Line (ballx, bally)-(ballx + BALLWIDTH, bally + BALLWIDTH - 1), frmG.GameArea.BackColor, BF
    rballx = X - BALLINDENT
    rbally = Y - BALLINDENT
    ballx = X
    bally = Y
    
    
End Sub


Sub UnclipCurs()
Dim screenrect As RECT
If lockMouse Then
    screenrect.left = 0
    screenrect.top = 0
    screenrect.right = Int(Screen.Width / Screen.TwipsPerPixelX)
    screenrect.bottom = Int(Screen.Height / Screen.TwipsPerPixelY)
    
    dum = ClipCursor(screenrect)
End If
End Sub

Function xblockat(ByVal X As Single) As Integer
' Determine the horizontal index of a block at position x.
' Assumes at least one row of blocks is present

Dim i, xblock As Integer
Dim found As Boolean

    found = False
    For i = 1 To blocksAcross
        If X >= theBlocks(i, 1).X And X <= theBlocks(i, 1).X + frmG.BlockImages.ImageWidth Then
            xblock = i
            found = True
            Exit For
        End If
    Next i
    
    If found Then xblockat = i Else xblockat = 0
End Function


Function yblockat(ByVal Y As Single) As Integer
' Determine the horizontal index of a block at position x.
' Assumes at least one col of blocks is present

Dim i, yblock As Integer
Dim found As Boolean

    found = False
    For i = 1 To blocksDown
        If Y >= theBlocks(1, i).Y And Y <= theBlocks(1, i).Y + frmG.BlockImages.ImageHeight Then
            yblock = i
            found = True
            Exit For
        End If
    Next i
    
    If found Then yblockat = i Else yblockat = 0
End Function


