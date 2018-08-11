#ifndef AUDIO_H_INCLUDED
#define AUDIO_H_INCLUDED

#include <StringType.h>
#include <vector>

class gxSound;
class gxChannel;
class Camera;
class Pivot;
class Object;

namespace CBN {

// Structs.
struct Sound {
private:
    static std::vector<Sound*> list;

public:
    Sound();
    ~Sound();
    static int getListSize();
    static Sound* getObject(int index);

    gxSound* internal;
    String file;
};

struct SoundManager {
private:
    static std::vector<SoundManager*> list;

public:
    SoundManager();
    ~SoundManager();
    static int getListSize();
    static SoundManager* getObject(int index);

    // Buttons
    Sound* button;
    Sound* buttonErr;

    // Footsteps
    Sound* footstep[8];
    Sound* footstepRun[8];
    Sound* footstepMetal[8];
    Sound* footstepMetalRun[8];
    Sound* footstepPD[3];
    Sound* footstep8601[3];

    // Doors
    Sound* openDoor[3];
    Sound* openBigDoor[2];
    Sound* openHCZDoor[3];

    Sound* closeDoor[3];
    Sound* closeBigDoor[2];
    Sound* closeHCZDoor[3];

    // Keycards
    Sound* keycardUse;
    Sound* keycardErr;
    Sound* scannerUse;
    Sound* scannerErr;

    // Pickups
    Sound* itemPick[4];

    // Elevator
    Sound* elevatorBeep;
    Sound* elevatorMove;

    // Tesla
    Sound* teslaIdle;
    Sound* teslaActive;
    Sound* teslaPowerUp;
    Sound* teslaShock;

    // Bullets
    Sound* gunshot[2];
    Sound* bulletHit;
    Sound* bulletMiss;

    Sound* alarm;
    Sound* caution;
    Sound* hiss;
    Sound* lightSwitch;
    Sound* lever;
    Sound* burst;
    Sound* camera;
    Sound* heartbeat;
};

struct SoundChannel {
private:
    static std::vector<SoundChannel*> list;

public:
    SoundChannel();
    ~SoundChannel();
    static int getListSize();
    static SoundChannel* getObject(int index);

    gxChannel* internal;

    Camera* camera;
    Pivot* point;

    float range;
    float volume;
};

// TODO: Add the ability to have no music playing.
// Make it possible to play a track then swap to default when said track is done (breath.ogg)
class MusicManager {
public:
    MusicManager();
    ~MusicManager();

    gxSound* currMusic;
    gxChannel* channel;

    // When set to true the music manager just plays whichever track
    // is appropriate to the current zone/menu.
    bool useDefault;

    String nowPlaying;
    String shouldPlay;

    // Used for fading out tracks.
    bool fadeOut;
    float currMusicVolume;

    void restoreDefaultMusic();
    void setNextMusicTrack(const String& trackName, int fadeOut = true);
    void freeMusic();
    void update();
};

// Constants.
extern const int STEPSOUND_DEFAULT;
extern const int STEPSOUND_METAL;
extern const String MUS_MENU;
extern const String MUS_INTRO;
extern const String MUS_LCZ;
extern const String MUS_EZ;
extern const String MUS_HCZ;
extern const String MUS_049;
extern const String MUS_079;
extern const String MUS_106;
extern const String MUS_205;
extern const String MUS_939;
extern const String MUS_8601;
extern const String MUS_8602;
extern const String MUS_14991;
extern const String MUS_14992;
extern const String MUS_PD;
extern const String MUS_TRENCH;
extern const String MUS_GATEA;
extern const String MUS_GATEB1;
extern const String MUS_GATEB2;
extern const String MUS_END;
extern const String MUS_BREATH;

// Globals.
extern SoundManager* sndManager;
extern MusicManager* musicManager;

// Functions.
SoundManager* CreateSoundManager();

void LoadInGameSounds(SoundManager* sndMan);

void DeloadInGameSounds(SoundManager* sndMan);

void AddPositionalChannel(gxChannel* ref, Camera* cam, Object* ent, float range = 10.0f, float vol = 1.0f);

void UpdateChannelList();

Sound* InitializeSound_SM(const String& fileName);

Sound* LoadSound_SM(const String& fileName);

gxChannel* PlaySound2(gxSound* snd);

gxChannel* PlaySound_SM(Sound* snd);

void FreeSound_SM(Sound* snd);

gxChannel* PlayRangedSound(gxSound* soundHandle, Camera* cam, Object* entity, float range = 10.0f, float volume = 1.0f);

gxChannel* PlayRangedSound_SM(Sound* snd, Camera* cam, Object* entity, float range = 10, float volume = 1.0);

gxChannel* LoopRangedSound(gxSound* soundHandle, gxChannel* chn, Camera* cam, Object* entity, float range = 10, float volume = 1.0);

gxChannel* LoopRangedSound_SM(Sound* snd, gxChannel* chn, Camera* cam, Object* entity, float range = 10, float volume = 1.0);

void UpdateRangedSoundOrigin(gxChannel* chn, Camera* cam, Object* entity, float range = 10.0f, float volume = 1.0f);

void UpdateRangedSoundOrigin_SM(SoundChannel* chn);

gxSound* LoadTempSound(const String& file);

gxSound* LoadEventSound(struct Event* e, String file, int i = 0);

void PauseSounds();

void ResumeSounds();

void StopSounds();

int GetMaterialStepSound(Object* entity);

}
#endif // AUDIO_H_INCLUDED