Type Launcher
    Field width%
    Field height%

    Field background%

	Field resWidths.IntArray
	Field resHeights.IntArray

    Field selectedGFXMode%
End Type
Global launcher.Launcher = Null

Function CreateLauncher.Launcher()
    Local launch.Launcher = New Launcher

    launch\width = Min(GetINIInt(OptionFile, "launcher", "launcher width"), 1024)
    launch\height = Min(GetINIInt(OptionFile, "launcher", "launcher height"), 768)

	launch\resWidths = CreateIntArray(CountGfxModes3D())
	launch\resHeights = CreateIntArray(CountGfxModes3D())

    Local i%
	For i = 1 To CountGfxModes3D()
		If ((GfxModeDepth(i) = 32)) Then
			SetIntArrayElem(launch\resWidths, GfxModeWidth(i), i - 1)
            SetIntArrayElem(launch\resHeights, GfxModeHeight(i), i - 1)
		EndIf
	Next

	launch\selectedGFXMode = VerifyResolution()

	Graphics3DExt(launch\width, launch\height, 0, 2)
	AppTitle("SCP - Containment Breach Launcher")

	MenuScale = 1

	launch\background = LoadImage("GFX/menu/launcher.jpg")

    InitializeUIAssets()

	SetBuffer(BackBuffer())

	SetFont(uiAssets\font[0])

    Return launch
End Function

Function DestroyLauncher(launch.Launcher)
    FreeImage(launch\background)

	ReleaseUIAssets()

    Delete launch
End Function

Function UpdateLauncher()
    Local x% = 40
    Local y% = 280 - 65

    Local i%
    For i = 1 To CountGfxModes3D()
        If ((GfxModeDepth(i) = 32)) Then
            If (MouseOn(x - 1, y - 1, 100, 20)) Then
                If (MouseHit1) Then launcher\selectedGFXMode = i-1
            EndIf

            y=y+20
            If ((y >= 240 - 65 + (launcher\height - 80 - 260))) Then
                y = 280 - 65
                x = x + 100
            EndIf
        EndIf
    Next

    ;-----------------------------------------------------------------
    x = 30
    y = 369

    y = y + 10
    For i = 1 To CountGfxDrivers()
        If (MouseOn(x - 1, y - 1, 290, 20)) Then
            If (MouseHit1) Then userOptions\gfxDriver = i
        EndIf

        y = y + 20
    Next

	userOptions\fullscreen = UpdateUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions\fullscreen)

    userOptions\launcher = UpdateUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions\launcher)

    If (UpdateUIButton(launcher\width - 30 - 90, launcher\height - 50 - 55, 100, 30, "LAUNCH", False)) Then
        userOptions\screenWidth = GetIntArrayElem(launcher\resWidths, launcher\selectedGFXMode)
        userOptions\screenHeight = GetIntArrayElem(launcher\resHeights, launcher\selectedGFXMode)

        userOptions\gfxDriver = userOptions\gfxDriver

        SaveOptionsINI()
        DestroyLauncher(launcher) : launcher = Null

		InitializeMainGame()

        Return
    EndIf

    If (UpdateUIButton(launcher\width - 30 - 90, launcher\height - 50, 100, 30, "EXIT", False)) Then End()
End Function

Function DrawLauncher()
	SetFont(uiAssets\font[0])

	Color(0,0,0)
    Rect(0, 0, launcher\width, launcher\height, True)

    Color(255, 255, 255)
    DrawImage(launcher\background, 0, 0)

    Text(20, 240 - 65, "Resolution: ")

    Local x% = 40
    Local y% = 280 - 65

    Local i%
    For i = 1 To CountGfxModes3D()
        If ((GfxModeDepth(i) = 32)) Then
            Color(0, 0, 0)

            If ((launcher\selectedGFXMode = (i-1))) Then
                Rect(x - 1, y - 1, 100, 20, False)
            EndIf

            Text(x, y, (GetIntArrayElem(launcher\resWidths, i - 1) + "x" + GetIntArrayElem(launcher\resHeights, i - 1)))
            If (MouseOn(x - 1, y - 1, 100, 20)) Then
                Color(100, 100, 100)
                Rect(x - 1, y - 1, 100, 20, False)
            EndIf

            y=y+20
            If ((y >= 240 - 65 + (launcher\height - 80 - 260))) Then
                y = 280 - 65
                x = x + 100
            EndIf
        EndIf
    Next

    Color(255, 255, 255)
    x = 30
    y = 369
    Rect(x - 10, y, 340, 95)
    Text(x - 10, y - 25, "Graphics:")

    y = y + 10
    For i = 1 To CountGfxDrivers()
        Color(0, 0, 0)
        If (userOptions\gfxDriver = i) Then Rect(x - 1, y - 1, 290, 20, False)
        ;text(x, y, bbGfxDriverName(i))

        LimitText(GfxDriverName(i), x, y, 290, False)
        If (MouseOn(x - 1, y - 1, 290, 20)) Then
            Color(100, 100, 100)
            Rect(x - 1, y - 1, 290, 20, False)
        EndIf

        y = y + 20
    Next

	DrawUITick(40 + 430 - 15, 260 - 55 + 5 - 8, userOptions\fullscreen)
    Text(40 + 430 + 15,       262 - 55 + 5 - 8, "Fullscreen")

    Color(255, 255, 255)
    ;Text(40 + 430 + 15, 262 - 55 + 35 - 8, "Borderless",False,False)
    ;Text(40 + 430 + 15, 262 - 55 + 35 + 12, "windowed mode",False,False)

    DrawUITick(40 + 430 - 15, 260 - 55 + 95 + 8, userOptions\launcher)
    Text(40 + 430 + 15,       262 - 55 + 95 + 8, "Use launcher")

    Text(40+ 260 + 15, 262 - 55 + 140, "Current Resolution: "+GetIntArrayElem(launcher\resWidths, launcher\selectedGFXMode) + "x" + GetIntArrayElem(launcher\resHeights, launcher\selectedGFXMode))

    ;If (GfxModeWidths(SelectedGFXMode)<G_viewport_width) Then
    ;	Text(40+ 260 + 65, 262 - 55 + 160, "(upscaled to")
    ;ElseIf (GfxModeWidths(SelectedGFXMode)>G_viewport_width) Then
    ;	Text(40+ 260 + 65, 262 - 55 + 160, "(downscaled to")
    ;EndIf

    ;Text(40+ 260 + 65, 262 - 55 + 180, G_viewport_width + "x" + G_viewport_height + ")")

    DrawUIButton(launcher\width - 30 - 90, launcher\height - 50 - 55, 100, 30, "LAUNCH", False)

    DrawUIButton(launcher\width - 30 - 90, launcher\height - 50, 100, 30, "EXIT", False)
    Flip()
End Function
;~IDEal Editor Parameters:
;~F#30
;~C#Blitz3D