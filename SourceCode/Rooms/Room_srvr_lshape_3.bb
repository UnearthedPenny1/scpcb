Function FillRoom_srvr_lshape_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
	Local it.Item, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x - 504.0 * RoomScale, -512.0 * RoomScale, r\z + 271.0 * RoomScale, True)
    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x + 628.0 * RoomScale, -512.0 * RoomScale, r\z + 271.0 * RoomScale, True)
    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x - 532.0 * RoomScale, -512.0 * RoomScale, r\z - 877.0 * RoomScale, True)

    it = CreatePaper("doc970", r\x + 960.0 * RoomScale, r\y - 448.0 * RoomScale, r\z + 251.0 * RoomScale)
    RotateEntity(it\collider, 0, r\angle, 0)
    EntityParent(it\collider, r\obj)

    it = CreateItem("gasmask", r\x + 954.0 * RoomScale, r\y - 504.0 * RoomScale, r\z + 235.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function