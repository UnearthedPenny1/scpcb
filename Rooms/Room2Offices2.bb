Function FillRoom2Offices2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    it = CreateItem("Level 1 Key Card", "key1", r\x - 368.0 * RoomScale, r\y - 48.0 * RoomScale, r\z + 80.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Document SCP-895", "paper", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z + 368.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    If Rand(2) = 1 Then
        it = CreateItem("Document SCP-860", "paper", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 464.0 * RoomScale)
    Else
        it = CreateItem("SCP-093 Recovered Materials", "paper", r\x - 800.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 464.0 * RoomScale)
    EndIf
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("S-NAV 300 Navigator", "nav", r\x - 336.0 * RoomScale, r\y - 48.0 * RoomScale, r\z - 480.0 * RoomScale)
    it\state = 28 : EntityParent(it\collider, r\obj)		
    
    r\Objects[0] = LoadMesh_Strict("GFX\npcs\duck_low_res.b3d")
    ScaleEntity(r\Objects[0], 0.07, 0.07, 0.07)
    
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x-808.0 * RoomScale, -72.0 * RoomScale, r\z - 40.0 * RoomScale, True)
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x-488.0 * RoomScale, 160.0 * RoomScale, r\z + 700.0 * RoomScale, True)
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x-488.0 * RoomScale, 160.0 * RoomScale, r\z - 668.0 * RoomScale, True)
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x-572.0 * RoomScale, 350.0 * RoomScale, r\z - 4.0 * RoomScale, True)
    
    temp = Rand(1,4)
    PositionEntity(r\Objects[0], EntityX(r\Objects[temp],True),EntityY(r\Objects[temp],True),EntityZ(r\Objects[temp],True),True)
End Function


Function UpdateEventRoom2offices2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If mainPlayer\blinkTimer<-8 And mainPlayer\blinkTimer >-12 Then
			temp = Rand(1,4)
			PositionEntity e\room\Objects[0], EntityX(e\room\Objects[temp],True),EntityY(e\room\Objects[temp],True),EntityZ(e\room\Objects[temp],True),True
			RotateEntity e\room\Objects[0], 0, Rnd(360), 0
		EndIf
	EndIf
	;[End Block]
End Function

