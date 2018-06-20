Function UpdateEvent_dimension1499(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;e\EventState: If player entered dimension (will be resetted after the player leaves it)
		;0: The player never entered SCP-1499
		;1: The player had already entered the dimension at least once
		;2: The player is in dimension
	If mainPlayer\currRoom = e\room Then
		If e\EventState < 2.0 Then
			;1499 random generator
			;[Block]
			;TODO: Reimplement.
;			If e\EventState = 0.0
;				If e\EventStr = ""
;					e\EventStr = "load0"
;				ElseIf e\EventStr = "load0"
;					e\room\Objects[0] = CreatePlane()
;					Local planetex% = LoadTexture("GFX/map/Rooms/dimension1499/grit3.jpg")
;					EntityTexture e\room\Objects[0],planetex%
;					FreeTexture planetex%
;					PositionEntity e\room\Objects[0],0,EntityY(e\room\obj),0
;					EntityType e\room\Objects[0],HIT_MAP
;					;EntityParent e\room\Objects[0],e\room\obj
;					e\EventStr = "load1"
;				ElseIf e\EventStr = "load1"
;					NTF_1499Sky = sky_CreateSky("GFX/map/sky/1499sky")
;					e\EventStr = 1
;				Else
;					If Int(e\EventStr)<16
;						e\room\Objects[Int(e\EventStr)] = LoadMesh("GFX/map/Rooms/dimension1499/1499object"+(Int(e/EventStr))+".b3d")
;						HideEntity e\room\Objects[Int(e\EventStr)]
;						e\EventStr = Int(e\EventStr)+1
;					ElseIf Int(e\EventStr)=16
;						CreateChunkParts(e\room)
;						e\EventStr = 17
;					ElseIf Int(e\EventStr) = 17
;						x# = EntityX(e\room\obj)
;						z# = EntityZ(e\room\obj)
;						Local ch.Chunk
;						For i = -2 To 2 Step 2
;							ch = CreateChunk(-1,x#*(i*2.5),EntityY(e\room\obj),z#)
;						Next
;						;If Music(18)=0 Then Music(18) = LoadSound("SFX/Music/1499.ogg")
;						e\EventState = 2.0
;						e\EventStr = 18
;					EndIf
;				EndIf
;			Else
;				e\EventState = 2.0
;			EndIf
			;[End Block]
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtype1499 Then
					n\Idle = False
					n\State = 0
					;If Rand(2)=1 Then n\State2 = 500*3
					n\Angle = Rnd(360)
					PositionEntity n\Collider,EntityX(n\Collider)+Rnd(-60.0,60.0),EntityY(n\Collider)+0.05,EntityZ(n\Collider)+Rnd(-60.0,60.0)
					ResetEntity n\Collider
				EndIf
			Next
		EndIf
		;PositionEntity e\room\Objects[0],0,800,0
		CameraFogRange mainPlayer\cam,40,80
		CameraFogColor mainPlayer\cam,96,97,104
		CameraClsColor mainPlayer\cam,96,97,104
		CameraRange mainPlayer\cam,0.05,90
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next
		ShowEntity e\room\obj
		
		UpdateChunks(e\room,15)
		ShowEntity NTF_1499Sky
		Update1499Sky()
		ShouldPlay = 18
		If EntityY(mainPlayer\collider)<800.0 Then PositionEntity mainPlayer\collider,EntityX(mainPlayer\collider),800.1,EntityZ(mainPlayer\collider),True
		ResetEntity mainPlayer\collider
			
		mainPlayer\footstepOverride=3
	Else
		If e\EventState = 2.0 Then
			HideEntity NTF_1499Sky
			HideChunks()
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtype1499 Then
					;RemoveNPC(n)
					n\Idle = True
				EndIf
			Next
			e\EventState = 1.0
		EndIf
	EndIf
	;[End Block]
End Function

Function UpdateLeave1499()
	Local r.Rooms, it.Items
	
	If (Not (IsPlayerWearingTempName(mainPlayer,"scp1499") Or IsPlayerWearingTempName(mainPlayer,"super1499"))) And mainPlayer\currRoom\RoomTemplate\Name$ = "dimension1499" Then
		For r.Rooms = Each Rooms
			If r = NTF_1499PrevRoom Then
				mainPlayer\blinkTimer = -1
				;Msg = "You removed the gas mask and reappeared inside the facility."
				;MsgTimer = 70 * 5
				NTF_1499X# = EntityX(mainPlayer\collider)
				NTF_1499Y# = EntityY(mainPlayer\collider)
				NTF_1499Z# = EntityZ(mainPlayer\collider)
				PositionEntity (mainPlayer\collider, NTF_1499PrevX#, NTF_1499PrevY#+0.05, NTF_1499PrevZ#)
				ResetEntity(mainPlayer\collider)
				UpdateDoors()
				UpdateRooms()
				For it.Items = Each Items
					it\disttimer = 0
					If it\itemtemplate\tempname = "scp1499" Or it\itemtemplate\tempname = "super1499" Then
						If EntityY(it\collider) >= EntityY(mainPlayer\currRoom\obj)-5 Then
							PositionEntity it\collider,NTF_1499PrevX#,NTF_1499PrevY#+(EntityY(it\collider)-EntityY(mainPlayer\currRoom\obj)),NTF_1499PrevZ#
							ResetEntity it\collider
						EndIf
					EndIf
				Next
				mainPlayer\currRoom = r
				PlaySound2(LoadTempSound(LoadSound("SFX/SCP/1499/Exit.ogg")))
				NTF_1499PrevX# = 0.0
				NTF_1499PrevY# = 0.0
				NTF_1499PrevZ# = 0.0
				NTF_1499PrevRoom = Null
				;Brightness = StoredBrightness
				Exit
			EndIf
		Next
	EndIf
	
End Function

;TODO: clean up
Type ChunkPart
	Field Amount%
	Field obj%[128]
	Field RandomYaw#[128]
	Field ID
End Type

Type Chunk
	Field obj%[128]
	Field x#,z#,y#
	Field Amount%
	;Field debugobj%
End Type

Function CreateChunkParts(r.Rooms)
	Local File$ = "Data/1499chunks.INI"
	Local ChunkAmount% = GetINIInt(File$,"general","count")
	Local i%,StrTemp$,j%
	Local chp.ChunkPart,chp2.ChunkPart
	Local obj%
	
	SeedRnd SeedStringToInt(RandomSeed)
	
	For i = 0 To ChunkAmount%
		Local loc% = GetINISectionLocation(File$,"chunk"+i)
		If loc > 0 Then
			StrTemp$ = GetINIString2(File,loc%,"count")
			chp = New ChunkPart
			chp\Amount% = Int(StrTemp$)
			DebugLog "------------------"
			For j = 0 To Int(StrTemp$)
				Local objID% = GetINIString2(File$,loc%,"obj"+j)
				Local x$ = GetINIString2(File$,loc%,"obj"+j+"-x")
				Local z$ = GetINIString2(File$,loc%,"obj"+j+"-z")
				Local yaw$ = GetINIString2(File$,loc%,"obj"+j+"-yaw")
				DebugLog "1499 chunk X/Z/Yaw: "+x$+"|"+z$+"|"+yaw$
				chp\obj%[j] = CopyEntity(r\Objects[objID%])
				If Lower(yaw$) = "random" Then
					chp\RandomYaw#[j] = Rnd(360)
					RotateEntity chp\obj[j],0,chp\RandomYaw[j],0
				Else
					RotateEntity chp\obj[j],0,Float(yaw),0
				EndIf
				PositionEntity chp\obj[j],Float(x),0,Float(z)
				ScaleEntity chp\obj[j],RoomScale,RoomScale,RoomScale
				EntityType chp\obj[j],HIT_MAP
				EntityPickMode chp\obj[j],2
				;EntityParent chp\obj[j],r\obj
			Next
			chp2 = Before(chp)
			If chp2 <> Null Then
				chp\ID = chp2\ID+1
			EndIf
			DebugLog "<<<<<<<<<<<<<<<<"
			DebugLog "Generated 1499 chunk "+chp\ID+" sucessfully"
		EndIf
	Next
	
	SeedRnd MilliSecs()
	
End Function

Function CreateChunk.Chunk(obj%,x#,y#,z#,spawnNPCs%=True)
	Local ch.Chunk = New Chunk
	Local chp.ChunkPart,i,n.NPCs
	
	;If obj%<>0
	;	ch\obj% = CopyEntity(obj%)
	;	PositionEntity ch\obj%,x,y,z
	;	ScaleEntity ch\obj%,RoomScale,RoomScale,RoomScale
	;	EntityType ch\obj%,HIT_MAP
	;EndIf
	
	;ch\debugobj% = CreateCube()
	;ScaleEntity ch\debugobj%,20,20,20
	;PositionEntity ch\debugobj%,x#,y#+20,z#
	;EntityColor ch\debugobj%,Rand(0,255),Rand(0,255),Rand(0,255)
	;EntityFX ch\debugobj%,1+FE_WIRE
	
	If obj% > -1 Then
		For chp = Each ChunkPart
			If chp\ID = obj% Then
				ch\Amount% = chp\Amount%
				For i = 0 To chp\Amount
					ch\obj[i] = CopyEntity(chp\obj[i])
					PositionEntity ch\obj[i],x#,y#,z#
					;ScaleEntity ch\obj[i],RoomScale,RoomScale,RoomScale
					MoveEntity ch\obj[i],EntityX(chp\obj[i]),0,EntityZ(chp\obj[i])
				Next
				Exit
			EndIf
		Next
		If spawnNPCs% Then
			For i = 0 To Rand(5,10)
				n.NPCs = CreateNPC(NPCtype1499,x+Rnd(-60.0,60.0),y+0.5,z+Rnd(-60.0,60.0))
				If Rand(2)=1 Then n\State2 = 500*3
				n\Angle = Rnd(360)
			Next
		EndIf
	EndIf
	
	ch\x = x
	ch\z = z
	ch\y = y
	
	Return ch
End Function

Function UpdateChunks(r.Rooms,ChunkPartAmount%,spawnNPCs%=True)
	Local ch.Chunk, ch2.Chunk, chp.ChunkPart, ChunkPartAmount2%
	Local ChunkHideDistance% = 120
	Local temp% = False, temp2% = False
	Local x#,z#,i%,y#,CurrChunkX#,CurrChunkZ#
	Local obj%
	
	For ch = Each Chunk
		;If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),ch\x,ch\z)<ChunkHideDistance
		;	;If ch\obj <> 0 Then ShowEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			ShowEntity ch\obj[i]
		;		Next
		;	EndIf
		;Else
		;	;If ch\obj <> 0 Then HideEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			HideEntity ch\obj[i]
		;		Next
		;	EndIf
		;EndIf
		If ch\obj[0]<>0 Then
			For i = 0 To ch\Amount
				ShowEntity ch\obj[i]
			Next
		EndIf
		y# = ch\y
		If Abs(EntityX(mainPlayer\collider)-ch\x)<20 Then
			If Abs(EntityZ(mainPlayer\collider)-ch\z)<20 Then
				CurrChunkX# = ch\x
				CurrChunkZ# = ch\z
			EndIf
		EndIf
	Next
	
	;CurrChunkX# = Int(EntityX(mainPlayer\collider)/40)*40
	;CurrChunkZ# = Int(EntityZ(mainPlayer\collider)/40)*40
	
	x# = -(ChunkHideDistance+(CurrChunkX#))
	z# = -(ChunkHideDistance+(CurrChunkZ#))
	
	Local StrTemp$ = ""
	SeedRnd SeedStringToInt(RandomSeed)
	
	Repeat
		temp2% = False
		For ch = Each Chunk
			If (ch\x=x#) And (ch\z=z#) Then
				temp2% = True
				Exit
			EndIf
		Next
		If (Not temp2%) Then
			;ch2 = CreateChunk(r\Objects[Rand(1,ChunkPartAmount%)],x#,y#,z#)
			ChunkPartAmount2 = GetINIInt("Data/1499chunks.INI","general","count")
			ch2 = CreateChunk(Rand(0,ChunkPartAmount2),x#,y#,z#,spawnNPCs%)
		EndIf
		If x# < (ChunkHideDistance+(CurrChunkX#)) Then
			x# = x# + 40
		Else
			If z# < (ChunkHideDistance+(CurrChunkZ#)) Then
				x# = -(ChunkHideDistance+(CurrChunkX#))
				z# = z# + 40
			Else
				Exit
			EndIf
		EndIf
	Forever
	
	SeedRnd MilliSecs()
	
End Function

Function HideChunks()
	Local ch.Chunk,i
	
	For ch = Each Chunk
		;If ch\obj <> 0 Then HideEntity ch\obj
		If ch\obj[0]<>0 Then
			For i = 0 To ch\Amount
				HideEntity ch\obj[i]
			Next
		EndIf
	Next
	
End Function

Function DeleteChunks()
	
	Delete Each Chunk
	Delete Each ChunkPart
	
End Function

;~IDEal Editor Parameters:
;~F#A0
;~C#Blitz3D