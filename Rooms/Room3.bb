

Function UpdateEventRoom3door(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If EntityDistance(e\room\obj,mainPlayer\collider)<2.5 Then
			For do.doors = Each Doors
				If Abs(EntityX(do\obj,True)-EntityX(mainPlayer\collider))<2.0 Then
					If Abs(EntityZ(do\obj,True)-EntityZ(mainPlayer\collider))<2.0 Then
						If (Not EntityInView(do\obj,mainPlayer\cam)) Then
							If do\open Then
								DebugLog "%@@= \ {2E6C2=FD gi`h]c"
								do\open = False
								do\openstate = 0
								mainPlayer\blurTimer = 100
								mainPlayer\camShake = 3.0											
							EndIf
							
						EndIf
						Exit
					EndIf
				EndIf
			Next
			RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
End Function



Function UpdateEvent106victim(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (Not Contained106) Then
		If mainPlayer\currRoom = e\room Then
			If e\EventState = 0 Then
				de.Decals = CreateDecal(0, EntityX(e\room\obj), 799.0*RoomScale, EntityZ(e\room\obj), -90, Rand(360), 0)
				de\Size = 0.05 : de\SizeChange = 0.0015 : EntityAlpha(de\obj, 0.8) : UpdateDecals()			
				;TODO: fix
				;PlaySound2(DecaySFX(3), mainPlayer\cam, de\obj, 15.0)
				e\EventState=1
			EndIf
		EndIf
		
		If e\EventState > 0 Then 
			If e\room\NPC[0]=Null Then
				e\EventState=e\EventState+FPSfactor
			EndIf
			If e\EventState>200 Then
				If e\room\NPC[0]=Null Then
					e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj), 900.0*RoomScale, EntityZ(e\room\obj))
					RotateEntity e\room\NPC[0]\Collider, 0, Rnd(360), 0, True
					tex=LoadTexture("GFX\NPCs\106victim.jpg")
					e\room\NPC[0]\texture = "GFX\NPCs\106victim.jpg"
					EntityTexture e\room\NPC[0]\obj, tex
					FreeTexture tex
					e\room\NPC[0]\State=6
					
					;PlaySound_Strict HorrorSFX(0)
					;TODO: fix
					;PlaySound2(DecaySFX(2), mainPlayer\cam, e\room\NPC[0]\Collider, 15.0)
				EndIf
				
				EntityType e\room\NPC[0]\Collider,HIT_PLAYER
				If EntityY(e\room\NPC[0]\Collider)>0.35 Then
					AnimateNPC(e\room\NPC[0], 1, 10, 0.12, False)
					dist# = EntityDistance(mainPlayer\collider,e\room\NPC[0]\Collider)
					If dist<0.8 Then ;get the player out of the way
						fdir# = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(e\room\NPC[0]\Collider,True),EntityZ(e\room\NPC[0]\Collider,True))
						TranslateEntity mainPlayer\collider,Cos(-fdir+90)*(dist-0.8)*(dist-0.8),0,Sin(-fdir+90)*(dist-0.8)*(dist-0.8)
					EndIf
					
					If EntityY(e\room\NPC[0]\Collider)>0.6 Then EntityType e\room\NPC[0]\Collider,0
				Else
					e\EventState=e\EventState+FPSfactor
					AnimateNPC(e\room\NPC[0], 11, 19, 0.25, False)
					If e\Sound=0 Then 
						;e\Sound = LoadSound("SFX\General\BodyFall.ogg")
						LoadEventSound(e,"SFX\General\BodyFall.ogg")
						PlaySound_Strict e\Sound
						
						de.Decals = CreateDecal(0, EntityX(e\room\obj), 0.001, EntityZ(e\room\obj), 90, Rand(360), 0)
						de\Size = 0.4 : EntityAlpha(de\obj, 0.8) : UpdateDecals()			
					EndIf
					
					If e\EventState>400 Then
						If e\Sound<>0 Then FreeSound_Strict e\Sound : e\Sound=0
						RemoveEvent(e)
					EndIf								
				EndIf
				
			EndIf
		EndIf
		
	EndIf
	
	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D