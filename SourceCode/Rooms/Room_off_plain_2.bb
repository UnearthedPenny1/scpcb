Function FillRoom_off_plain_2(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    it = CreatePaper("doc106", r\x + 404.0 * RoomScale, r\y + 145.0 * RoomScale, r\z + 559.0 * RoomScale)
    EntityParent(it\collider, r\obj)

    it = CreateItem("navigator", r\x + 305.0 * RoomScale, r\y + 153.0 * RoomScale, r\z + 944.0 * RoomScale)
    it\state = 20 : EntityParent(it\collider, r\obj)

    it = CreatePaper("914_organic", r\x -137.0 * RoomScale, r\y + 153.0 * RoomScale, r\z + 464.0 * RoomScale)
    EntityParent(it\collider, r\obj)

	;TODO: fix?
    ;w.waypoints = CreateWaypoint(r\x - 32.0 * RoomScale, r\y + 66.0 * RoomScale, r\z + 288.0 * RoomScale, Null, r)
    ;w2.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z - 448.0 * RoomScale, Null, r)
    ;w\connected[0] = w2 : w\dist[0] = EntityDistance(w\obj, w2\obj)
    ;w2\connected[0] = w : w2\dist[0] = w\dist[0]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D