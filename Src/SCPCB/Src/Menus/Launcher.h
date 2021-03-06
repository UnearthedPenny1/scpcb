#ifndef LAUNCHER_H_INCLUDED
#define LAUNCHER_H_INCLUDED
#include <vector>

namespace CBN {

// Structs.
struct Launcher {
private:
    static std::vector<Launcher*> list;

    void verifyResolution();
public:
    Launcher();
    ~Launcher();
    void update();
    void draw();

    int width;
    int height;

    class bbImage* background;

    std::vector<int> resWidths;
    std::vector<int> resHeights;

    int selectedGFXMode;
};

// Globals.
extern Launcher* launcher;

}
#endif // LAUNCHER_H_INCLUDED
