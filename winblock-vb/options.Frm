VERSION 5.00
Object = "{6B7E6392-850A-101B-AFC0-4210102A8DA7}#1.1#0"; "COMCTL32.OCX"
Begin VB.Form frmOptions 
   Caption         =   "WinBlock Options"
   ClientHeight    =   5490
   ClientLeft      =   4125
   ClientTop       =   3000
   ClientWidth     =   5160
   Icon            =   "options.frx":0000
   LinkTopic       =   "Form1"
   PaletteMode     =   1  'UseZOrder
   ScaleHeight     =   5490
   ScaleWidth      =   5160
   Begin VB.CommandButton cmdDefaults 
      Caption         =   "&Defaults"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   120
      TabIndex        =   13
      Top             =   5040
      Width           =   1215
   End
   Begin VB.Frame Frame3 
      Caption         =   "Control"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   1095
      Left            =   120
      TabIndex        =   11
      Top             =   3840
      Width           =   4935
      Begin VB.CheckBox LockCheck 
         Caption         =   "&Lock mouse into game area during play"
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
         Left            =   960
         TabIndex        =   12
         Top             =   360
         Width           =   3255
      End
      Begin VB.Image Image1 
         Height          =   480
         Left            =   120
         Picture         =   "options.frx":1BB2
         Top             =   360
         Width           =   480
      End
   End
   Begin VB.Frame Frame2 
      Caption         =   "Sound"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   1095
      Left            =   120
      TabIndex        =   7
      Top             =   2640
      Width           =   4935
      Begin VB.CheckBox SoundsCheck 
         Caption         =   "&Use sound"
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
         Left            =   960
         TabIndex        =   8
         Top             =   360
         Value           =   1  'Checked
         Width           =   1695
      End
      Begin VB.Image Image2 
         Height          =   480
         Left            =   120
         Picture         =   "options.frx":1EBC
         Top             =   240
         Width           =   480
      End
   End
   Begin VB.CommandButton cmdCancel 
      Caption         =   "&Cancel"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   2520
      TabIndex        =   6
      Top             =   5040
      Width           =   1215
   End
   Begin VB.CommandButton cmdOK 
      Caption         =   "&OK"
      Default         =   -1  'True
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   3840
      TabIndex        =   5
      Top             =   5040
      Width           =   1215
   End
   Begin VB.Frame Frame1 
      Caption         =   "Ball"
      BeginProperty Font 
         Name            =   "Tahoma"
         Size            =   8.25
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   2415
      Left            =   120
      TabIndex        =   0
      Top             =   120
      Width           =   4935
      Begin VB.Timer Timer1 
         Interval        =   1
         Left            =   480
         Top             =   1320
      End
      Begin ComctlLib.TreeView BallList 
         Height          =   1215
         Left            =   1440
         TabIndex        =   10
         Top             =   960
         Width           =   3375
         _ExtentX        =   5953
         _ExtentY        =   2143
         _Version        =   327680
         LabelEdit       =   1
         Style           =   1
         ImageList       =   "SmallBalls"
         Appearance      =   1
         BeginProperty Font {0BE35203-8F91-11CE-9DE3-00AA004BB851} 
            Name            =   "Tahoma"
            Size            =   8.25
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         MouseIcon       =   "options.frx":21C6
      End
      Begin ComctlLib.Slider SpeedSlider 
         Height          =   495
         Left            =   1800
         TabIndex        =   2
         Top             =   360
         Width           =   2535
         _ExtentX        =   4471
         _ExtentY        =   873
         _Version        =   327680
         MouseIcon       =   "options.frx":21E2
         Min             =   1
         Max             =   30
         SelStart        =   10
         TickFrequency   =   5
         Value           =   10
      End
      Begin VB.Label Label4 
         Caption         =   "Sprite:"
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
         Left            =   720
         TabIndex        =   9
         Top             =   960
         Width           =   495
      End
      Begin ComctlLib.ImageList SmallBalls 
         Left            =   840
         Top             =   1320
         _ExtentX        =   1005
         _ExtentY        =   1005
         BackColor       =   -2147483643
         ImageWidth      =   16
         ImageHeight     =   16
         MaskColor       =   12632256
         _Version        =   327680
         BeginProperty Images {0713E8C2-850A-101B-AFC0-4210102A8DA7} 
            NumListImages   =   7
            BeginProperty ListImage1 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":21FE
               Key             =   ""
            EndProperty
            BeginProperty ListImage2 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":2310
               Key             =   ""
            EndProperty
            BeginProperty ListImage3 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":2422
               Key             =   ""
            EndProperty
            BeginProperty ListImage4 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":2534
               Key             =   ""
            EndProperty
            BeginProperty ListImage5 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":2646
               Key             =   ""
            EndProperty
            BeginProperty ListImage6 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":2758
               Key             =   ""
            EndProperty
            BeginProperty ListImage7 {0713E8C3-850A-101B-AFC0-4210102A8DA7} 
               Picture         =   "options.frx":286A
               Key             =   ""
            EndProperty
         EndProperty
      End
      Begin VB.Label Label3 
         Caption         =   "Fast"
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
         Left            =   4440
         TabIndex        =   4
         Top             =   360
         Width           =   375
      End
      Begin VB.Label Label2 
         Caption         =   "Slow"
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
         Left            =   1440
         TabIndex        =   3
         Top             =   360
         Width           =   375
      End
      Begin VB.Label Label1 
         Caption         =   "Speed:"
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
         Left            =   720
         TabIndex        =   1
         Top             =   360
         Width           =   1095
      End
      Begin VB.Image BallIcon 
         Height          =   480
         Left            =   120
         Picture         =   "options.frx":297C
         Top             =   240
         Width           =   480
      End
   End
End
Attribute VB_Name = "frmOptions"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub Slider1_Click()

End Sub


Private Sub cmdCancel_Click()
    frmOptions.Hide
End Sub

Private Sub cmdDefaults_Click()
Dim prompt As String
    prompt = "Resetting to defaults will remove all current"
    prompt = prompt + Chr$(13) + "preferences, including the highest score table."
    prompt = prompt + Chr$(13) + "Are you sure you want to do this?"
    
    reply = MsgBox(prompt, vbYesNo + vbExclamation, "Default Preferences")
    If reply = vbYes Then
        SetRegDefaults
        GetRegSettings
           frmOptions.Hide
    End If
 
    
End Sub

Private Sub cmdOK_Click()
    ballspeed = SpeedSlider.Value
    If SoundsCheck = 1 Then playsounds = True Else playsounds = False
    If LockCheck = 1 Then
        lockMouse = True
        If ismovable Then ClipCurs
    Else
        lockMouse = False
    End If
    ballSprite = BallList.SelectedItem.Index
    SetRegSettings
    frmOptions.Hide
End Sub

Private Sub Form_Load()
    SpeedSlider = ballspeed
    If playsounds Then SoundsCheck.Value = 1 Else SoundsCheck.Value = 0
    If lockMouse Then LockCheck = 1 Else LockCheck = 0
    BallList.Nodes.Add , , "test", "Blue", BLUE
    BallList.Nodes.Add , , , "Cyan", CYAN
    BallList.Nodes.Add , , , "Green", GREEN
    BallList.Nodes.Add , , , "Grey", GREY
    BallList.Nodes.Add , , , "Red", RED
    BallList.Nodes.Add , , , "Yellow", YELLOW
    BallList.Nodes.Add , , , "Black", BLACK
    BallList.Nodes(ballSprite).Selected = True
  '  BallIcon.Picture = frmG.SmallSprites.ListImages(ballSprite).Picture
    BallList.Refresh
End Sub

Private Sub Timer1_Timer()
   BallIcon.Picture = frmG.SmallSprites.ListImages(BallList.SelectedItem.Index).Picture
End Sub


