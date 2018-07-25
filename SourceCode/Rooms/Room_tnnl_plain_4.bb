

Function UpdateEvent_tnnl_plain_4(e.Event)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%, tex%

	Local p.Particle, n.NPC, r.Room, e2.Event, it.Item, em.Emitter, sc.SecurityCam, sc2.SecurityCam

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\room\dist < 10.0 And e\room\dist > 0) Then
		e\room\npc[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj,True)+1.0, 0.5, EntityZ(e\room\obj,True)+1.0)
		e\room\npc[0]\texture = "GFX/npcs/body1.jpg"
		tex = LoadTexture(e\room\npc[0]\texture)
		EntityTexture(e\room\npc[0]\obj, tex)
		FreeTexture(tex)

		RotateEntity(e\room\npc[0]\collider, 0, EntityYaw(e\room\obj)-(Rand(20,60)),0, True)

		SetNPCFrame(e\room\npc[0], 19)
		e\room\npc[0]\state=8

		;Delete e
		RemoveEvent(e)
	EndIf
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D