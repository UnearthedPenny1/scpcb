#include <bbblitz3d.h>
#include <bbaudio.h>
#include <bbmath.h>

#include "Objects.h"
#include "Assets.h"
#include "Player.h"
#include "GameMain.h"
#include "Menus/Menu.h"
#include "MathUtils/MathUtils.h"
#include "Audio.h"
#include "Doors.h"
#include "NPCs/NPCs.h"
#include "Events.h"
#include "MapSystem.h"

namespace CBN {

// Structs.
std::vector<Lever*> Lever::list;
Lever::Lever() {
    list.push_back(this);
}
Lever::~Lever() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int Lever::getListSize() {
    return list.size();
}
Lever* Lever::getObject(int index) {
    return list[index];
}

std::vector<ElevatorObj*> ElevatorObj::list;
ElevatorObj::ElevatorObj() {
    list.push_back(this);
}
ElevatorObj::~ElevatorObj() {
    for (int i = 0; i < list.size(); i++) {
        if (list[i] == this) {
            list.erase(list.begin() + i);
            break;
        }
    }
}
int ElevatorObj::getListSize() {
    return list.size();
}
ElevatorObj* ElevatorObj::getObject(int index) {
    return list[index];
}

// Functions.
MeshModel* CreateButton(float x, float y, float z, float pitch, float yaw, float roll) {
    MeshAssetWrap* buttonObj = MeshAssetWrap::grab("GFX/Map/Meshes/button.b3d");
    MeshModel* obj = bbDeepCopyMesh(buttonObj->getMesh());
    buttonObj->drop();

    bbScaleEntity(obj, 0.03, 0.03, 0.03);

    bbPositionEntity(obj, x,y,z);
    bbRotateEntity(obj, pitch,yaw,roll);

    bbEntityPickMode(obj, 2);

    return obj;
}

void UpdateButton(MeshModel* obj) {
    //entityDistance(collider, d\buttons[i])
    float dist = bbEntityDistance(mainPlayer->collider, obj);

    if (dist < 0.8) {
        Pivot* tempPvt = bbCreatePivot();
        bbPositionEntity(tempPvt, bbEntityX(mainPlayer->cam), bbEntityY(mainPlayer->cam), bbEntityZ(mainPlayer->cam));
        bbPointEntity(tempPvt,obj);

        if (bbEntityPick(tempPvt, 0.65) == obj) {
            if (mainPlayer->closestButton == 0) {
                mainPlayer->closestButton = obj;
            } else {
                if (dist < bbEntityDistance(mainPlayer->collider, mainPlayer->closestButton)) {
                    mainPlayer->closestButton = obj;
                }
            }
        }

        bbFreeEntity(tempPvt);
    }
}

Lever* CreateLever() {
    MeshAssetWrap* leverObj = MeshAssetWrap::grab("GFX/Map/Meshes/leverhandle.b3d");
    MeshAssetWrap* leverBaseObj = MeshAssetWrap::grab("GFX/Map/Meshes/leverbase.b3d");

    Lever* lever = new Lever();
    lever->obj = bbCopyMeshModelEntity(leverObj->getMesh());
    lever->baseObj = bbCopyMeshModelEntity(leverBaseObj->getMesh());
    leverObj->drop();
    leverBaseObj->drop();

    return lever;
}

void UpdateLevers() {
    for (int i = 0; i < Lever::getListSize(); i++) {
        Lever* lever = Lever::getObject(i);

        float dist = bbEntityDistance(mainPlayer->cam, lever->obj);

        if (dist < 8.0) {
            if (dist < 0.8 & (!lever->locked)) {
                if (bbEntityInView(lever->obj, mainPlayer->cam)) {

                    bbEntityPick(mainPlayer->cam, 0.65);

                    if (bbPickedEntity() == lever->obj) {
                        DrawHandIcon = true;
                        if (MouseHit1) {
                            mainPlayer->grabbedEntity = lever->obj;
                        }
                    }

                    float prevpitch = bbEntityPitch(lever->obj);

                    if (MouseDown1 | MouseHit1) {
                        if (mainPlayer->grabbedEntity != 0) {
                            if (mainPlayer->grabbedEntity == lever->obj) {
                                DrawHandIcon = true;
                                //TurnEntity(lever\obj, , 0, 0)
                                bbRotateEntity(mainPlayer->grabbedEntity, Max(Min(bbEntityPitch(lever->obj)+Max(Min(mouse_y_speed_1 * 8,30.0),-30), 80), -80), bbEntityYaw(lever->obj), 0);

                                mainPlayer->drawDirectionalArrow[0] = true;
                                mainPlayer->drawDirectionalArrow[2] = true;

                            }
                        }
                    }

                    if (bbEntityPitch(lever->obj,true) > 75) {
                        if (prevpitch <= 75) {
                            PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, lever->obj, 1.0);
                        }
                    } else if ((bbEntityPitch(lever->obj,true) < -75)) {
                        if (prevpitch >= -75) {
                            PlayRangedSound_SM(sndManager->lever, mainPlayer->cam, lever->obj, 1.0);
                        }
                    }
                }
            }

            if (MouseDown1==false & MouseHit1==false) {
                if (bbEntityPitch(lever->obj,true) > 0) {
                    bbRotateEntity(lever->obj, CurveValue(80, bbEntityPitch(lever->obj), 10), bbEntityYaw(lever->obj), 0);
                } else {
                    bbRotateEntity(lever->obj, CurveValue(-80, bbEntityPitch(lever->obj), 10), bbEntityYaw(lever->obj), 0);
                }
                mainPlayer->grabbedEntity = 0;
            }

        }

        if (bbEntityPitch(lever->obj, true) > 0) {
            lever->succ = true;
        } else {
            lever->succ = false;
        }
    }
}

float UpdateElevators(float state, Door* door1, Door* door2, class Object* room1, class Object* room2, Event* event) {
    door1->isElevatorDoor = 1;
    door2->isElevatorDoor = 1;
    if (door1->open == true & door2->open == false) {
        state = -1;
        if ((mainPlayer->closestButton == door2->buttons[0] | mainPlayer->closestButton == door2->buttons[1]) & MouseHit1) {
            UseDoor(door1,false);
        }
        if (door2->npcCalledElevator == true) {
            UseDoor(door1,false);
            door2->npcCalledElevator = 2;
        }
    } else if ((door2->open == true & door1->open == false)) {
        state = 1;
        if ((mainPlayer->closestButton == door1->buttons[0] | mainPlayer->closestButton == door1->buttons[1]) & MouseHit1) {
            UseDoor(door2,false);
        }
        if (door1->npcCalledElevator == true) {
            UseDoor(door2,false);
            door1->npcCalledElevator = 2;
        }
    } else if ((abs(door1->openstate-door2->openstate)<0.2)) {
        door1->isElevatorDoor = 2;
        door2->isElevatorDoor = 2;
    }

    int inside = false;
    NPC* NPC_inside = nullptr;

    //molemmat ovet kiinni = hissi liikkuu
    if (door1->open == false & door2->open == false) {
        door1->locked = true;
        door2->locked = true;
        door1->npcCalledElevator = 2;
        door2->npcCalledElevator = 2;
        //ylh��lt� alas
        if (state < 0) {
            state = state - timing->tickDuration;
            //pelaaja hissin sis�ll�
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room1,true))<280.0*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true))<280.0*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true))<280.0*RoomScale) {
                        inside = true;

                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }

                        mainPlayer->camShake = bbSin(abs(state)/3.0)*0.3;
                    }
                }
            }

            for (int i = 0; i < NPC::getListSize(); i++) {
                NPC* n = NPC::getObject(i);

                if (n->canUseElevator) {
                    if (abs(bbEntityX(n->collider)-bbEntityX(room1,true))<280.0*RoomScale) {
                        if (abs(bbEntityZ(n->collider)-bbEntityZ(room1,true))<280.0*RoomScale) {
                            if (abs(bbEntityY(n->collider)-bbEntityY(room1,true))<280.0*RoomScale) {
                                NPC_inside = n;
                            }
                        }
                    }
                }
            }

            if (NPC_inside != nullptr & (!inside)) {
                NPC_inside->idle = true;
            } else if ((NPC_inside != nullptr & inside)) {
                NPC_inside->idle = false;
            }

            if (state < -400) {
                door1->locked = false;
                door2->locked = false;
                door1->npcCalledElevator = false;
                door2->npcCalledElevator = false;
                state = 0;

                if (inside) {
                    float x = Max(Min((bbEntityX(mainPlayer->collider)-bbEntityX(room1,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    float z = Max(Min((bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    bbPositionEntity(mainPlayer->collider, bbEntityX(room2,true)+x,0.1+bbEntityY(room2,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);
                    bbResetEntity(mainPlayer->collider);
                    UpdateDoorsTimer = 0;
                    mainPlayer->dropSpeed = 0;
                    UpdateDoors();
                    UpdateRooms();
                }

                if (NPC_inside != nullptr) {
                    float x = Max(Min((bbEntityX(NPC_inside->collider)-bbEntityX(room1,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    float z = Max(Min((bbEntityZ(NPC_inside->collider)-bbEntityZ(room1,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    bbPositionEntity(NPC_inside->collider, bbEntityX(room2,true)+x,0.1+bbEntityY(room2,true)+(bbEntityY(NPC_inside->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);
                    bbResetEntity(NPC_inside->collider);
                    UpdateDoorsTimer = 0;
                    NPC_inside->dropSpeed = 0;
                    if ((int)(NPC_inside->idle)) {
                        bbTurnEntity(NPC_inside->obj,0,180,0);
                        bbTurnEntity(NPC_inside->collider,0,180,0);
                        NPC_inside->idle = false;
                    }
                    NPC_inside->currElevator = nullptr;
                }

                UseDoor(door2,false);

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room2, 4.0);
            }
            //alhaalta yl�s
        } else {
            state = state + timing->tickDuration;
            //pelaaja hissin sis�ll�
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room2,true))<280.0*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true))<280.0*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true))<280.0*RoomScale) {
                        inside = true;

                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }

                        mainPlayer->camShake = bbSin(abs(state)/3.0)*0.3;
                    }
                }
            }

            for (int i = 0; i < NPC::getListSize(); i++) {
                NPC* n = NPC::getObject(i);

                if (n->canUseElevator) {
                    if (abs(bbEntityX(n->collider)-bbEntityX(room2,true))<280.0*RoomScale) {
                        if (abs(bbEntityZ(n->collider)-bbEntityZ(room2,true))<280.0*RoomScale) {
                            if (abs(bbEntityY(n->collider)-bbEntityY(room2,true))<280.0*RoomScale) {
                                NPC_inside = n;
                            }
                        }
                    }
                }
            }

            if (NPC_inside != nullptr & (!inside)) {
                NPC_inside->idle = true;
            } else if ((NPC_inside != nullptr & inside)) {
                NPC_inside->idle = false;
            }

            if (state > 400) {
                door1->locked = false;
                door2->locked = false;
                door1->npcCalledElevator = false;
                door2->npcCalledElevator = false;
                state = 0;

                //pelaaja hissin sis�ll�, siirret��n
                if (inside) {
                    float x = Max(Min((bbEntityX(mainPlayer->collider)-bbEntityX(room2,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    float z = Max(Min((bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    bbPositionEntity(mainPlayer->collider, bbEntityX(room1,true)+x,0.1+bbEntityY(room1,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);
                    bbResetEntity(mainPlayer->collider);
                    UpdateDoorsTimer = 0;
                    mainPlayer->dropSpeed = 0;
                    UpdateDoors();
                    UpdateRooms();
                }

                if (NPC_inside != nullptr) {
                    float x = Max(Min((bbEntityX(NPC_inside->collider)-bbEntityX(room2,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    float z = Max(Min((bbEntityZ(NPC_inside->collider)-bbEntityZ(room2,true)),280*RoomScale-0.17),-280*RoomScale+0.17);
                    bbPositionEntity(NPC_inside->collider, bbEntityX(room1,true)+x,0.1+bbEntityY(room1,true)+(bbEntityY(NPC_inside->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);
                    bbResetEntity(NPC_inside->collider);
                    UpdateDoorsTimer = 0;
                    NPC_inside->dropSpeed = 0;
                    if ((int)(NPC_inside->idle)) {
                        bbTurnEntity(NPC_inside->obj,0,180,0);
                        bbTurnEntity(NPC_inside->collider,0,180,0);
                        NPC_inside->idle = false;
                    }
                    NPC_inside->currElevator = nullptr;
                }

                UseDoor(door1,false);

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room1, 4.0);
            }

        }
    }

    return state;

}

float UpdateElevators2(float State, Door* door1, Door* door2, class Object* room1, class Object* room2, Event* event) {
    float x;
    float z;

    door1->isElevatorDoor = 1;
    door2->isElevatorDoor = 1;
    if (door1->open == true & door2->open == false) {
        State = -1;
        door2->isElevatorDoor = 2;
        if ((mainPlayer->closestButton == door2->buttons[0] | mainPlayer->closestButton == door2->buttons[1]) & MouseHit1) {
            UseDoor(door1,false);
        }
    } else if ((door2->open == true & door1->open == false)) {
        State = 1;
        door1->isElevatorDoor = 2;
        if ((mainPlayer->closestButton == door1->buttons[0] | mainPlayer->closestButton == door1->buttons[1]) & MouseHit1) {
            UseDoor(door2,false);
        }
    } else if ((abs(door1->openstate-door2->openstate)<0.2)) {
        door1->isElevatorDoor = 2;
        door2->isElevatorDoor = 2;
    }

    int inside = false;
    float dist;
    float dir;

    //molemmat ovet kiinni = hissi liikkuu
    if (door1->open == false & door2->open == false) {
        door1->locked = true;
        door2->locked = true;
        //ylh��lt?alas
        if (State < 0) {
            State = State - timing->tickDuration;
            //pelaaja hissin sis�ll?
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room1,true))<280.0*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room1,true))<280.0*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true))<280.0*RoomScale) {
                        inside = true;

                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }

                        mainPlayer->camShake = bbSin(abs(State)/3.0)*0.3;
                    }
                }
            }

            if (State < -400) {
                door1->locked = false;
                door2->locked = false;
                State = 0;

                UseDoor(door2,true);

                if (inside) {

                    dist = Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room1,true),bbEntityZ(room1,true));

                    dir = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room1,true),bbEntityZ(room1,true));
                    //EntityYaw(room2,True)+angleDist(dir,EntityYaw(room1,True))
                    dir = dir+bbEntityYaw(room2,true)-bbEntityYaw(room1,true);
                    //dir=dir-90.0

                    //					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room1,True)+EntityYaw(room2,True)

                    dir = WrapAngle(dir);

                    x = Max(Min(bbCos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17);
                    z = Max(Min(bbSin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17);

                    //x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
                    //z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)

                    //dir
                    bbRotateEntity(mainPlayer->collider,bbEntityPitch(mainPlayer->collider,true),bbEntityYaw(room2,true)+angleDist(bbEntityYaw(mainPlayer->collider,true),bbEntityYaw(room1,true)),bbEntityRoll(mainPlayer->collider,true),true);

                    bbPositionEntity(mainPlayer->collider, bbEntityX(room2,true)+x,0.05+bbEntityY(room2,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room1,true)),bbEntityZ(room2,true)+z,true);

                    bbResetEntity(mainPlayer->collider);
                    UpdateDoors();
                    UpdateRooms();
                }

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room2, 4.0);
                //PlaySound(ElevatorBeepSFX)
            }
            //alhaalta yl�s
        } else {
            State = State + timing->tickDuration;
            //pelaaja hissin sis�ll?
            if (abs(bbEntityX(mainPlayer->collider)-bbEntityX(room2,true))<280.0*RoomScale) {
                if (abs(bbEntityZ(mainPlayer->collider)-bbEntityZ(room2,true))<280.0*RoomScale) {
                    if (abs(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true))<280.0*RoomScale) {
                        inside = true;

                        if (!bbChannelPlaying(event->soundChannels[0])) {
                            event->soundChannels[0] = PlaySound_SM(sndManager->elevatorMove);
                        }

                        mainPlayer->camShake = bbSin(abs(State)/3.0)*0.3;
                    }
                }
            }

            if (State > 400) {
                door1->locked = false;
                door2->locked = false;
                State = 0;

                UseDoor(door1,true);

                //pelaaja hissin sis�ll? siirret��n
                if (inside) {

                    dist = Distance(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room2,true),bbEntityZ(room2,true));

                    dir = GetAngle(bbEntityX(mainPlayer->collider,true),bbEntityZ(mainPlayer->collider,true),bbEntityX(room2,true),bbEntityZ(room2,true));
                    //EntityYaw(room1,True)+angleDist(dir,EntityYaw(room2,True))
                    dir = dir+bbEntityYaw(room1,true)-bbEntityYaw(room2,true);
                    //dir=dir-90.0

                    //					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room2,True)+EntityYaw(room1,True)

                    //dir=WrapAngle(dir)

                    x = Max(Min(bbCos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17);
                    z = Max(Min(bbSin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17);

                    //x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
                    //z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)

                    //dir
                    bbRotateEntity(mainPlayer->collider, bbEntityPitch(mainPlayer->collider,true),bbEntityYaw(room2,true)+angleDist(bbEntityYaw(mainPlayer->collider,true),bbEntityYaw(room1,true)),bbEntityRoll(mainPlayer->collider,true),true);

                    bbPositionEntity(mainPlayer->collider, bbEntityX(room1,true)+x,0.05+bbEntityY(room1,true)+(bbEntityY(mainPlayer->collider)-bbEntityY(room2,true)),bbEntityZ(room1,true)+z,true);

                    bbResetEntity(mainPlayer->collider);
                    UpdateDoors();
                    UpdateRooms();
                }

                PlayRangedSound_SM(sndManager->elevatorBeep, mainPlayer->cam, room1, 4.0);
            }

        }
    }

    return State;

}

ElevatorObj* AssignElevatorObj(MeshModel* obj, Door* door, int in_facility) {
    ElevatorObj* eo = new ElevatorObj();

    eo->obj = obj;
    eo->door = door;
    eo->inFacility = in_facility;

    return eo;
}

}