Function FillRoom_cont_049_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x + 640.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x - 2032.0 * RoomScale, -3280.0 * RoomScale, r\z - 656.0 * RoomScale, True)

    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x - 640.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale, True)

    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x + 2040.0 * RoomScale, -3280.0 * RoomScale, r\z + 656.0 * RoomScale, True)

    ;storage room (the spawn point of scp-049)
    ;r\objects[5] = CreatePivot(r\obj)
    ;PositionEntity(r\objects[5], r\x + 584.0 * RoomScale, -3440.0 * RoomScale, r\z + 104.0 * RoomScale, True)

    ;zombie 1 and 049
    r\objects[4] = CreatePivot(r\obj)
    PositionEntity(r\objects[4], r\x + 528.0 * RoomScale, -3440.0 * RoomScale, r\z + 96.0 * RoomScale, True)
    ;zombie 2
    r\objects[5] = CreatePivot(r\obj)
    PositionEntity(r\objects[5], r\x  + 64.0 * RoomScale, -3440.0 * RoomScale, r\z - 1000.0 * RoomScale, True)

	Local n%
    For n = 0 To 1
		r\levers[n] = CreateLever()

		ScaleEntity(r\levers[n]\obj, 0.03, 0.03, 0.03)
		ScaleEntity(r\levers[n]\baseObj, 0.03, 0.03, 0.03)

		Select n
			Case 0 ;power feed
				PositionEntity(r\levers[n]\obj, r\x - 328.0 * RoomScale, r\y - 3374.0 * RoomScale, r\z + 916 * RoomScale, True)
				PositionEntity(r\levers[n]\baseObj, r\x - 328.0 * RoomScale, r\y - 3374.0 * RoomScale, r\z + 916 * RoomScale, True)

			Case 1 ;generator
				PositionEntity(r\levers[n]\obj, r\x - 370.0 * RoomScale, r\y - 3400.0 * RoomScale, r\z - 799 * RoomScale, True)
				PositionEntity(r\levers[n]\baseObj, r\x - 370.0 * RoomScale, r\y - 3400.0 * RoomScale, r\z - 799 * RoomScale, True)
		End Select

		EntityParent(r\levers[n]\obj, r\obj)
		EntityParent(r\levers[n]\baseObj, r\obj)

        RotateEntity(r\objects[n*2+6], 0, -180*n, 0)
        RotateEntity(r\levers[n]\obj, 81-92*n, -180*(Not n), 0)

        EntityPickMode(r\levers[n]\obj, 1, False)
        EntityRadius(r\levers[n]\obj, 0.1)
    Next


    r\doors[0] = CreateDoor(r\x + 328.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\doors[0]\autoClose = False : r\doors[0]\open = True
    PositionEntity(r\doors[0]\buttons[1], r\x + 288.0 * RoomScale, 0.7, r\z + 512.0 * RoomScale, True)
    PositionEntity(r\doors[0]\buttons[0], r\x + 368.0 * RoomScale, 0.7, r\z + 840.0 * RoomScale, True)

    r\doors[1] = CreateDoor(r\x - 2328.0 * RoomScale, -3520.0 * RoomScale, r\z - 656.0 * RoomScale, 90, r, False)
    r\doors[1]\autoClose = False : r\doors[1]\open = False
    PositionEntity(r\doors[1]\buttons[1], r\x - 2432.0 * RoomScale, EntityY(r\doors[1]\buttons[1],True), r\z - 816.0 * RoomScale, True)
    PositionEntity(r\doors[1]\buttons[0], r\x - 2304.0 * RoomScale, EntityY(r\doors[1]\buttons[0],True), r\z - 472.0 * RoomScale, True)

    r\doors[2] = CreateDoor(r\x - 328.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\doors[2]\autoClose = False : r\doors[2]\open = True
    PositionEntity(r\doors[2]\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 512.0 * RoomScale, True)
    PositionEntity(r\doors[2]\buttons[1], r\x - 368.0 * RoomScale, 0.7, r\z - 840.0 * RoomScale, True)

    r\doors[3] = CreateDoor(r\x + 2360.0 * RoomScale, -3520.0 * RoomScale, r\z + 656.0 * RoomScale, 90, r, False)
    r\doors[3]\autoClose = False : r\doors[3]\open = False
    PositionEntity(r\doors[3]\buttons[0], r\x + 2432.0 * RoomScale, EntityY(r\doors[3]\buttons[0],True), r\z + 816.0 * RoomScale, True)
    PositionEntity(r\doors[3]\buttons[1], r\x + 2312.0 * RoomScale, EntityY(r\doors[3]\buttons[1],True), r\z + 472.0 * RoomScale, True)

    For i = 0 To 3
        If ((i Mod 2) = 1) Then
            AssignElevatorObj(r\objects[i],r\doors[i],2)
        Else
            AssignElevatorObj(r\objects[i],r\doors[i],True)
        EndIf
    Next

    ;storage room door
    r\doors[4] = CreateDoor(r\x + 272.0 * RoomScale, -3552.0 * RoomScale, r\z + 104.0 * RoomScale, 90, r, False)
    r\doors[4]\autoClose = False : r\doors[4]\open = False : r\doors[4]\locked = True

    d = CreateDoor(r\x,0,r\z, 0, r, False, DOOR_TYPE_HCZ, r\roomTemplate\name)

    it = CreatePaper("doc049", r\x - 608.0 * RoomScale, r\y - 3332.0 * RoomScale, r\z + 876.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("firstaid", r\x +385.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 271.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    ;r\objects[10] = LoadMesh("GFX/Map/room049_hb.b3d",r\obj)
    ;EntityPickMode(r\objects[10],2)
    ;EntityType(r\objects[10],HIT_MAP)
    ;EntityAlpha(r\objects[10],0.0)
End Function


Function UpdateEvent_cont_049_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (mainPlayer\currRoom = e\room) Then
		If (EntityY(mainPlayer\collider) > -2848*RoomScale) Then
			e\eventState2 = UpdateElevators(e\eventState2, e\room\doors[0], e\room\doors[1],e\room\objects[0],e\room\objects[1], e)
			e\eventState3 = UpdateElevators(e\eventState3, e\room\doors[2], e\room\doors[3],e\room\objects[2],e\room\objects[3], e)
			e\overwriteMusic = False
		Else
			e\overwriteMusic = True

			If (e\eventState = 0) Then
				If (Not e\loaded) Then
					n = CreateNPC(NPCtypeZombie, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True),EntityZ(e\room\objects[4],True))
					PointEntity(n\collider, e\room\obj)
					TurnEntity(n\collider, 0, 190, 0)

					n = CreateNPC(NPCtypeZombie, EntityX(e\room\objects[5],True),EntityY(e\room\objects[5],True),EntityZ(e\room\objects[5],True))
					PointEntity(n\collider, e\room\obj)
					TurnEntity(n\collider, 0, 20, 0)

					For n = Each NPC
						If (n\npcType = NPCtype049) Then
							e\room\npc[0]=n
							e\room\npc[0]\state = 2
							e\room\npc[0]\idle = 1
							PositionEntity(e\room\npc[0]\collider,EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True)+3,EntityZ(e\room\objects[4],True))
							ResetEntity(e\room\npc[0]\collider)
							Exit
						EndIf
					Next
					If (e\room\npc[0]=Null) Then
						n = CreateNPC(NPCtype049, EntityX(e\room\objects[4],True), EntityY(e\room\objects[4],True)+3, EntityZ(e\room\objects[4],True))
						PointEntity(n\collider, e\room\obj)
						n\state = 2
						n\idle = 1
						e\room\npc[0]=n
					EndIf

					e\musicTrack = MUS_049
					e\loaded = True
					e\eventState=1
				EndIf
			ElseIf (e\eventState > 0) Then

				temp = (Not e\room\levers[0]\succ) ;power feed
				x = e\room\levers[1]\succ ;generator

				e\room\doors[1]\locked = True
				e\room\doors[3]\locked = True

				If (temp Or Int(x)) Then ;TODO: really
					;049 appears when either of the levers is turned
					e\eventState = Max(e\eventState,70*180)

					If (temp And Int(x)) Then
						e\room\doors[1]\locked = False
						e\room\doors[3]\locked = False
						e\eventState2 = UpdateElevators(e\eventState2, e\room\doors[0], e\room\doors[1],e\room\objects[0],e\room\objects[1], e)
						e\eventState3 = UpdateElevators(e\eventState3, e\room\doors[2], e\room\doors[3],e\room\objects[2],e\room\objects[3], e)

						If (e\sounds[1]=0) Then LoadEventSound(e,"SFX/General/GeneratorOn.ogg",1)
						e\soundChannels[1]=LoopRangedSound(e\sounds[1], e\soundChannels[1], mainPlayer\cam, e\room\levers[1]\baseObj, 6.0, e\eventState3)

						If (e\room\npc[0]\idle > 0) Then
							i = 0
							If (EntityDistance(mainPlayer\collider,e\room\doors[1]\frameobj)<3.0) Then
								i = 1
							ElseIf (EntityDistance(mainPlayer\collider,e\room\doors[3]\frameobj)<3.0) Then
								i = 3
							EndIf
							If (i > 0) Then
								;If EntityVisible(mainPlayer\collider,e\room\doors[i]\frameobj)
								PositionEntity(e\room\npc[0]\collider,EntityX(e\room\objects[i],True),EntityY(e\room\objects[i],True),EntityZ(e\room\objects[i],True))
								ResetEntity(e\room\npc[0]\collider)
								PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, e\room\objects[i], 4.0)
								UseDoor(e\room\doors[i],False)
								e\room\doors[i-1]\open = False
								e\room\doors[i]\open = True
								e\room\npc[0]\pathStatus = FindPath(e\room\npc[0],EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
								PlayRangedSound(LoadTempSound("SFX/SCP/049/Greeting"+Str(Rand(1,2))+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
								e\room\npc[0]\idle = 0
								;EndIf
							EndIf
						EndIf
					EndIf
				EndIf

				If (e\eventState < 70*190) Then
					e\eventState = Min(e\eventState+timing\tickDuration,70*190)
					;049 spawns after 3 minutes
					If (e\eventState > 70*180) Then

						;If (e\room\npc[0]=Null) Then
						;	For n.NPC = Each NPC
						;		If (n\npcType=NPCtype049) Then e\room\npc[0]=n : Exit
						;	Next
						;EndIf
						;e\room\npc[0]\state = 1

						e\room\doors[4]\open = True
						PlaySound_SM(sndManager\teslaPowerUp)
						PlayRangedSound_SM(sndManager\openDoor[Rand(0,2)], mainPlayer\cam, e\room\doors[4]\obj, 6.0)

						e\room\doors[1]\open = False
						e\room\doors[3]\open = False
						e\room\doors[0]\open = True
						e\room\doors[2]\open = True

						e\eventState= 70*190
					EndIf
				ElseIf (e\eventState < 70*240) Then

					;If (e\room\npc[0]=Null) Then
					;	For n.NPC = Each NPC
					;		If (n\npcType=NPCtype049) Then e\room\npc[0]=n : Exit
					;	Next
					;Else
						;If (EntityDistance(e\room\npc[0]\collider,mainPlayer\collider)<4.0) Then
						;	e\eventState=e\eventState+timing\tickDuration
						;	If (e\eventState > 70*195 And e\eventState-timing\tickDuration =< 70*195) Then
						;		For n.NPC = Each NPC ;awake the zombies
						;			If (n\npcType = NPCtypeZombie And n\state = 0) Then
						;				n\state = 1
						;				SetNPCFrame(n, 155)
						;			EndIf
						;		Next
						;		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Greeting"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
						;	ElseIf e\eventState > 70*214 And e\eventState-timing\tickDuration =< 70*214
						;		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Spotted"+Rand(1,2)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
						;	ElseIf e\eventState > 70*227 And e\eventState-timing\tickDuration =< 70*227
						;		;PlayRangedSound(LoadTempSound("SFX/SCP/049/Detected"+Rand(1,3)+".ogg"),mainPlayer\cam, e\room\npc[0]\collider)
						;		e\eventState=70*241
						;	EndIf
						;EndIf
					;EndIf

					For n = Each NPC ;awake the zombies
						If (n\npcType = NPCtypeZombie And n\state = 0) Then
							n\state = 1
							SetNPCFrame(n, 155)
						EndIf
					Next
					e\eventState=70*241
				EndIf
			EndIf
		EndIf
	Else
		e\eventState2 = UpdateElevators(e\eventState2, e\room\doors[0], e\room\doors[1],e\room\objects[0],e\room\objects[1], e)
		e\eventState3 = UpdateElevators(e\eventState3, e\room\doors[2], e\room\doors[3],e\room\objects[2],e\room\objects[3], e)
		e\overwriteMusic = False
	EndIf

	If (e\eventState < 0) Then
		If (e\eventState > -70*4) Then
			If (mainPlayer\fallTimer => 0) Then
				mainPlayer\fallTimer = Min(-1, mainPlayer\fallTimer)
				PositionEntity(mainPlayer\head, EntityX(mainPlayer\cam, True), EntityY(mainPlayer\cam, True), EntityZ(mainPlayer\cam, True), True)
				ResetEntity(mainPlayer\head)
				RotateEntity(mainPlayer\head, 0, EntityYaw(mainPlayer\cam) + Rand(-45, 45), 0)
			ElseIf (mainPlayer\fallTimer < -230) Then
				mainPlayer\fallTimer = -231
				mainPlayer\blinkTimer = 0
				e\eventState = e\eventState-timing\tickDuration

				If (e\eventState =< -70*4) Then
					UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
					ShowEntity(mainPlayer\collider)
					mainPlayer\dropSpeed = 0
					mainPlayer\blinkTimer = -10
					mainPlayer\fallTimer = 0
					PositionEntity(mainPlayer\collider, EntityX(e\room\obj,True), EntityY(e\room\objects[5],True)+0.2, EntityZ(e\room\obj,True))
					ResetEntity(mainPlayer\collider)

					PositionEntity(e\room\npc[0]\collider, EntityX(e\room\objects[0],True),EntityY(e\room\objects[0],True),EntityZ(e\room\objects[0],True),True)
					ResetEntity(e\room\npc[0]\collider)

					For n = Each NPC
						If (n\npcType = NPCtypeZombie) Then
							PositionEntity(n\collider, EntityX(e\room\objects[4],True),EntityY(e\room\objects[4],True),EntityZ(e\room\objects[4],True),True)
							ResetEntity(n\collider)
							n\state = 4
							DebugLog("moving zombie")
						EndIf
					Next

					n = CreateNPC(NPCtypeMTF, EntityX(e\room\objects[5],True), EntityY(e\room\objects[5],True)+0.2, EntityZ(e\room\objects[5],True))
					n\state = 6
					n\reload = 6*70
					PointEntity(n\collider,mainPlayer\collider)
					e\room\npc[1] = n

					n = CreateNPC(NPCtypeMTF, EntityX(e\room\objects[5],True), EntityY(e\room\objects[5],True)+0.2, EntityZ(e\room\objects[5],True))
					n\state = 6
					n\reload = (6*70)+Rnd(15,30)
					RotateEntity(n\collider,0,EntityYaw(e\room\npc[1]\collider),0)
					MoveEntity(n\collider,0.5,0,0)
					PointEntity(n\collider,mainPlayer\collider)

					n = CreateNPC(NPCtypeMTF, EntityX(e\room\objects[5],True), EntityY(e\room\objects[5],True)+0.2, EntityZ(e\room\objects[5],True))
					n\state = 6
					;n\reload = 70*4.75
					n\reload = 10000
					RotateEntity(n\collider,0,EntityYaw(e\room\npc[1]\collider),0)
					n\state2 = EntityYaw(n\collider)
					MoveEntity(n\collider,-0.65,0,0)
					e\room\npc[2] = n

					MoveEntity(e\room\npc[1]\collider,0,0,0.1)
					PointEntity(mainPlayer\collider, e\room\npc[1]\collider)

					PlaySound2(LoadTempSound("SFX/Character/MTF/049/Player0492_1.ogg"))

					LoadEventSound(e,"SFX/SCP/049/0492Breath.ogg")

					IsZombie = True
				EndIf
			EndIf
		Else
			mainPlayer\blurTimer = 800
			mainPlayer\forceMove = 0.5
			mainPlayer\injuries = Max(2.0,mainPlayer\injuries)
			mainPlayer\bloodloss = 0

			;Msg = ""

			If (e\room\npc[2]\state = 7) Then
				If (e\room\npc[2]\state3 < 70*1.75) Then
					e\room\npc[2]\state3 = e\room\npc[2]\state3 + timing\tickDuration
				Else
					e\room\npc[2]\state = 6
					e\room\npc[2]\reload = e\room\npc[1]\reload+Rnd(5,10)
				EndIf
			ElseIf (e\room\npc[2]\state = 6 And e\room\npc[2]\reload > 70*4) Then
				If (e\room\npc[2]\state3 > -(70*4)) Then
					e\room\npc[2]\state3 = e\room\npc[2]\state3 - timing\tickDuration
				Else
					e\room\npc[2]\state3 = 0.0
					e\room\npc[2]\reload = 45
					e\room\npc[2]\state = 7
				EndIf
			EndIf

			pvt = CreatePivot()
			PositionEntity(pvt,EntityX(e\room\npc[1]\collider),EntityY(e\room\npc[1]\collider)+0.2,EntityZ(e\room\npc[1]\collider))

			PointEntity(mainPlayer\collider, e\room\npc[1]\collider)
			PointEntity(mainPlayer\cam, pvt)

			FreeEntity(pvt)

			If (mainPlayer\dead = True) Then
				If (IsChannelPlaying(e\room\npc[1]\soundChannels[0])) Then StopChannel(e\room\npc[1]\soundChannels[0])
				PlaySound2(LoadTempSound("SFX/Character/MTF/049/Player0492_2.ogg"))
				RemoveEvent(e)
			Else
				If (e\soundChannels[0] = 0) Then
					e\soundChannels[0] = PlaySound(e\sounds[0])
				Else
					If (Not IsChannelPlaying(e\soundChannels[0])) Then e\soundChannels[0] = PlaySound(e\sounds[0])
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D