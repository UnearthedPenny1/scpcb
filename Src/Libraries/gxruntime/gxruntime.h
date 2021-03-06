
#ifndef GXRUNTIME_H
#define GXRUNTIME_H

#include <windows.h>
#include <vector>

#include "gxaudio.h"
#include "gxinput.h"
#include "gxgraphics.h"
#include "gxfilesystem.h"
#include "gxtimer.h"

#include "StringType.h"

class gxRuntime{
	/***** INTERNAL INTERFACE *****/
public:

	HWND hwnd;
	HINSTANCE hinst;

	gxAudio *audio;
	gxInput *input;
	gxGraphics *graphics;
	gxFileSystem *fileSystem;

	void flip( bool vwait );
	void moveMouse( int x,int y );

	LRESULT windowProc( HWND hwnd,UINT msg,WPARAM w,LPARAM l );

	struct GfxMode;
	struct GfxDriver;

private:
	gxRuntime( HINSTANCE hinst,const String& cmd_line,HWND hwnd );
	~gxRuntime();

	void paint();
	void suspend();
	void forceSuspend();
	void resume();
	void forceResume();
	void backupWindowState();
	void restoreWindowState();

	RECT t_rect;
	int t_style;
	String cmd_line;
	bool pointer_visible;
	String app_title;
	String app_close;

	bool setDisplayMode( int w,int h,int d,bool d3d,IDirectDraw7 *dd );
	gxGraphics *openWindowedGraphics( int w,int h,int d,bool d3d );
	gxGraphics *openExclusiveGraphics( int w,int h,int d,bool d3d );

	bool enum_all;
	std::vector<GfxDriver*> drivers;
	GfxDriver *curr_driver;
	int use_di;

	void enumGfx();
	void denumGfx();

	void resetInput();
	void pauseAudio();
	void resumeAudio();
	void backupGraphics();
	void restoreGraphics();
	void acquireInput();
	void unacquireInput();

	/***** APP INTERFACE *****/
public:
	static gxRuntime *openRuntime( HINSTANCE hinst,const String& cmd_line );
	static void closeRuntime( gxRuntime *runtime );

	void asyncStop();
	void asyncRun();
	void asyncEnd();

	/***** GX INTERFACE *****/
public:
	enum{
		GFXMODECAPS_3D=1
	};

	//return true if program should continue, or false for quit.
	bool idle();
	bool delay( int ms );

	bool execute( const String& cmd );
	void setTitle( const String& title,const String& close );
	int  getMilliSecs();
	void setPointerVisible( bool vis );

	String commandLine();

	String systemProperty( const String& t );

	void debugStop();
	void debugProfile( int per );
	void debugStmt( int pos,const char *file );
	void debugEnter( void *frame,void *env,const char *func );
	void debugLeave();
	void debugInfo( const char *t );
	void debugError( const char *t );
	void debugLog( const char *t );

	int numGraphicsDrivers();
	void graphicsDriverInfo( int driver,String* name,int *caps );

	int numGraphicsModes( int driver );
	void graphicsModeInfo( int driver,int mode,int *w,int *h,int *d,int *caps );

	void windowedModeInfo( int *caps );

	gxAudio *openAudio( int flags );
	void closeAudio( gxAudio *audio );

	gxInput *openInput( int flags );
	void closeInput( gxInput *input );

	gxGraphics *openGraphics( int w,int h,int d,int driver,int flags );
	void closeGraphics( gxGraphics *graphics );
	bool graphicsLost();

	gxFileSystem *openFileSystem( int flags );
	void closeFileSystem( gxFileSystem *filesys );

	gxTimer *createTimer( int hertz );
	void freeTimer( gxTimer *timer );

	void enableDirectInput( bool use );
	int  directInputEnabled(){ return use_di; }

	OSVERSIONINFO osinfo;
};

#endif