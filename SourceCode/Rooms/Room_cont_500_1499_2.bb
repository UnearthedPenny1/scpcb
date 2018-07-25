Function FillRoom_cont_500_1499_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    r\doors[0] = CreateDoor(r\x + 288.0*RoomScale, r\y, r\z + 576.0*RoomScale, 90, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    r\doors[0]\open = False : r\doors[0]\locked = True
    d = CreateDoor(r\x + 777.0*RoomScale, r\y, r\z + 671.0*RoomScale, 90, r)
    d = CreateDoor(r\x + 556.0*RoomScale, r\y, r\z + 296.0*RoomScale, 0, r)
    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0],r\x + 576.0*RoomScale,r\y+160.0*RoomScale,r\z+632.0*RoomScale)
    EntityParent(r\objects[0],r\obj)

    it = CreateItem("scp1499", r\x + 600.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 228.0 * RoomScale)
    RotateEntity(it\collider, 0, r\angle, 0)
    EntityParent(it\collider, r\obj)

    it = CreateItem("badge", r\x + 364.0 * RoomScale, r\y + 5.0 * RoomScale, r\z + 716.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_cont_500_1499_2(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam, de.Decal

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;If mainPlayer\currRoom = e\room
	If (e\room\dist < 15) Then
		If (Contained106) Then e\eventState = 2.0
		If (Curr106\state < 0) Then e\eventState = 2.0

		If (e\eventState < 2.0) Then
			If (e\eventState = 0.0) Then
				LoadEventSound(e,"SFX/Character/Scientist/EmilyScream.ogg")
				e\soundChannels[0] = PlayRangedSound(e\sounds[0], mainPlayer\cam, e\room\objects[0], 100, 1.0)
				de = CreateDecal(0, EntityX(e\room\objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\objects[0],True), 90, Rand(360), 0)
				de\size = 0.5 : EntityAlpha(de\obj, 0.8)
				EntityFX(de\obj,1)
				e\eventState = 1.0
			ElseIf (e\eventState = 1.0) Then
				If (Not IsChannelPlaying(e\soundChannels[0])) Then
					e\eventState = 2.0
					e\room\doors[0]\locked = False
				Else
					UpdateRangedSoundOrigin(e\soundChannels[0],mainPlayer\cam,e\room\objects[0],100,1.0)
				EndIf
			EndIf
		Else
			DebugLog("Removed 'room2scps2' event")
			e\room\doors[0]\locked = False
			de = CreateDecal(0, EntityX(e\room\objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\objects[0],True), 90, Rand(360), 0)
			de\size = 0.5 : EntityAlpha(de\obj, 0.8)
			EntityFX(de\obj,1)
			RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D