VERSION 5.00
Object = "{6B7E6392-850A-101B-AFC0-4210102A8DA7}#1.1#0"; "COMCTL32.OCX"
Begin VB.Form frmG 
   BorderStyle     =   1  'Fixed Single
   Caption         =   "WinBlock"
   ClientHeight    =   5265
   ClientLeft      =   3780
   ClientTop       =   3120
   ClientWidth     =   6705
   ForeColor       =   &H00000000&
   Icon            =   "blockmain.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MouseIcon       =   "blockmain.frx":1BB2
   PaletteMode     =   1  'UseZOrder
   ScaleHeight     =   351
   ScaleMode       =   3  'Pixel
   ScaleWidth      =   447
   Begin VB.Timer BallTimer 
      Enabled         =   0   'False
      Interval        =   1
      Left            =   120
      Top             =   3840
   End
   Begin VB.PictureBox GameArea 
      AutoRedraw      =   -1  'True
      AutoSize        =   -1  'True
      BackColor       =   &H00000000&
      Height          =   4335
      Left            =   0
      MouseIcon       =   "blockmain.frx":1D04
      MousePointer    =   99  'Custom
      ScaleHeight     =   285
      ScaleMode       =   3  'Pixel
      ScaleWidth      =   437
      TabIndex        =   0
      Top             =   0
      Width           =   6615
   End
   Begin VB.Label Version 
      Caption         =   "version"
      BeginProperty Font 
         Name            =   "MS Serif"
         Size            =   6.75
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   4440
      TabIndex        =   7
      Top             =   4440
      Width           =   615
   End
   Begin VB.Label Label4 
      Caption         =   "(C) 1996/7 Brian Duff"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   2400
      TabIndex        =   6
      Top             =   4920
      Width           =   1935
   End
   Begin VB.Label Label1 
      Caption         =   "WinBlock"
      BeginProperty Font 
         Name            =   "Comic Sans MS"
         Size            =   21.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   -1  'True
         Strikethrough   =   0   'False
      EndProperty
      Height          =   495
      Left            =   2280
      TabIndex        =   5
      Top             =   4320
      Width           =   2175
   End
   Begin VB.Label Label3 
      BackStyle       =   0  'Transparent
      Caption         =   "Score"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   5160
      TabIndex        =   4
      Top             =   4440
      Width           =   1455
   End
   Begin VB.Label Label2 
      BackStyle       =   0  'Transparent
      Caption         =   "Lives Remaining"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   120
      TabIndex        =   3
      Top             =   4440
      Width           =   1455
   End
   Begin VB.Label Lives 
      Alignment       =   2  'Center
      BackColor       =   &H00000000&
      BorderStyle     =   1  'Fixed Single
      Caption         =   "0"
      BeginProperty Font 
         Name            =   "Times New Roman"
         Size            =   18
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      ForeColor       =   &H0000FF00&
      Height          =   495
      Left            =   120
      TabIndex        =   2
      Top             =   4680
      Width           =   1455
   End
   Begin VB.Label Score 
      Alignment       =   2  'Center
      BackColor       =   &H00000000&
      BorderStyle     =   1  'Fixed Single
      Caption         =   "0"
      BeginProperty Font 
         Name            =   "Times New Roman"
         Size            =   18
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      ForeColor       =   &H0000FF00&
      Height          =   495
      Left            =   5160
      TabIndex        =   1
      Top             =   4680
      Width           =   1455
   End
   Begin ComctlLib.ImageList SmallSprites 
      Left            =   4560
      Top             =   4560
      _ExtentX        =   1005
      _ExtentY        =   1005
      BackColor       =   -2147483643
      ImageWidth      =   32
      ImageHeight     =   32
      MaskColor       =   12632256
      _Version        =   327680
      BeginProperty Images {0713E8C2-850A-101B-AFC0-4210102A8DA7} 
         NumListImages   =   7
         BeginProperty ListImage1 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":1E56
            Key             =   ""
         EndProperty
         BeginProperty ListImage2 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":2170
            Key             =   ""
         EndProperty
         BeginProperty ListImage3 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":248A
            Key             =   ""
         EndProperty
         BeginProperty ListImage4 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":27A4
            Key             =   ""
         EndProperty
         BeginProperty ListImage5 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":2ABE
            Key             =   ""
         EndProperty
         BeginProperty ListImage6 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":2DD8
            Key             =   ""
         EndProperty
         BeginProperty ListImage7 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":30F2
            Key             =   ""
         EndProperty
      EndProperty
   End
   Begin ComctlLib.ImageList LongSprites 
      Left            =   1680
      Top             =   4560
      _ExtentX        =   1005
      _ExtentY        =   1005
      BackColor       =   -2147483643
      ImageWidth      =   69
      ImageHeight     =   16
      MaskColor       =   12632256
      _Version        =   327680
      BeginProperty Images {0713E8C2-850A-101B-AFC0-4210102A8DA7} 
         NumListImages   =   1
         BeginProperty ListImage1 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":340C
            Key             =   "pnormal"
            Object.Tag             =   "pnormal"
         EndProperty
      EndProperty
   End
   Begin ComctlLib.ImageList BlockImages 
      Left            =   5760
      Top             =   4200
      _ExtentX        =   1005
      _ExtentY        =   1005
      BackColor       =   8421376
      ImageWidth      =   51
      ImageHeight     =   22
      MaskColor       =   12632256
      _Version        =   327680
      BeginProperty Images {0713E8C2-850A-101B-AFC0-4210102A8DA7} 
         NumListImages   =   4
         BeginProperty ListImage1 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":3C4A
            Key             =   ""
         EndProperty
         BeginProperty ListImage2 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":4500
            Key             =   ""
         EndProperty
         BeginProperty ListImage3 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":4DB6
            Key             =   ""
         EndProperty
         BeginProperty ListImage4 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
            Picture         =   "blockmain.frx":566C
            Key             =   ""
         EndProperty
      EndProperty
   End
   Begin VB.Shape BotMarker 
      DrawMode        =   7  'Invert
      Height          =   1095
      Left            =   0
      Top             =   4200
      Width           =   375
   End
   Begin VB.Menu mnuGame 
      Caption         =   "&Game"
      Begin VB.Menu mnuGameNewGame 
         Caption         =   "&New Game"
         Shortcut        =   {F2}
      End
      Begin VB.Menu mnuGamePause 
         Caption         =   "&Pause"
         Shortcut        =   {F3}
      End
      Begin VB.Menu mnuGameSep1 
         Caption         =   "-"
      End
      Begin VB.Menu mnuGameOptions 
         Caption         =   "&Options..."
         Shortcut        =   ^O
      End
      Begin VB.Menu mnuGameSep2 
         Caption         =   "-"
      End
      Begin VB.Menu mnuGameExit 
         Caption         =   "E&xit"
      End
   End
   Begin VB.Menu mnuHelp 
      Caption         =   "&Help"
      Begin VB.Menu mnuHelpContents 
         Caption         =   "&Contents"
         Shortcut        =   {F1}
      End
      Begin VB.Menu mnuHelpSep1 
         Caption         =   "-"
      End
      Begin VB.Menu mnuHelpAbout 
         Caption         =   "&About WinBlock..."
         Shortcut        =   ^A
      End
   End
End
Attribute VB_Name = "frmG"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Sub idleAnimate()


End Sub

Private Sub balltimer_Timer()
    If ballismoving Then moveball
End Sub

Private Sub Form_Load()
    ' default options
    ballspeed = 10
    playsounds = True
    ballSprite = BLUE
    lockMouse = True
    
    
    GetRegSettings
    Version.Caption = Str$(App.Major) + "." + Str$(App.Minor)
    ismovable = False
    ballismoving = False
    mnuGamePause.Enabled = False
   ' lives.ForeColor = 0
  '  score.ForeColor = 0
    idleAnimate
    
    
   ' initGame
End Sub


Private Sub hiddenpic_Click()

End Sub


Private Sub GameArea_Click()
' if the ball is not moving, we want to activate it
If Not ballismoving And ismovable Then releaseball
End Sub

Private Sub GameArea_KeyDown(KeyCode As Integer, Shift As Integer)
    If ismovable Then
        If KeyCode = 100 Or KeyCode = 37 Then   ' left arrow
            movepaddle -1, Shift
            If Not ballismoving Then centreball
        ElseIf KeyCode = 102 Or KeyCode = 39 Then ' right arrow
            movepaddle 1, Shift
            If Not ballismoving Then centreball
        End If
    End If
            
End Sub


Private Sub GameArea_MouseMove(Button As Integer, Shift As Integer, X As Single, Y As Single)
    If ismovable Then
        jumppaddle X
        If Not ballismoving Then
            centreball
        End If
    End If
    
    
End Sub


Private Sub mnuGameExit_Click()
    End
    
End Sub

Private Sub mnuGameNewGame_Click()
    mnuGameNewGame.Enabled = False
    mnuGamePause.Enabled = True
    initGame
End Sub


Private Sub mnuGameOptions_Click()
    frmOptions.Show 1
End Sub

Private Sub mnuGamePause_Click()
    If mnuGamePause.Checked = False Then
        mnuGamePause.Checked = True
        BallTimer.Enabled = False
        ismovable = False
        UnclipCurs
    Else
        mnuGamePause.Checked = False
        BallTimer.Enabled = True
        ismovable = True
        ClipCurs
    End If
    
End Sub


Private Sub mnuHelpAbout_Click()
    frmAbout.Show 1
End Sub


