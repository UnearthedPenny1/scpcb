#include "../gxruntime/StringType.h"

#include "bbsys.h"
#include "bbruntime.h"

void  bbEnd(){
	RTEX( 0 );
}
void  bbStop(){
	gx_runtime->debugStop();
}

void  bbAppTitle( const String& ti,const String& cp ){
	gx_runtime->setTitle( ti,cp );
}

void  bbRuntimeError( const String& str ){
    String s = str;
	if( s.size()>255 ) s = s.substr(0,255);
	static char err[256];
	strcpy( err,s.cstr() );
	RTEX( err );
}

int bbExecFile( const String& f ){
	int n=gx_runtime->execute( f );
	return n;
}

void  bbDelay( int ms ){
	!gx_runtime->delay( ms );
}

int  bbMilliSecs(){
	return gx_runtime->getMilliSecs();
}

String  bbCommandLine(){
	return gx_runtime->commandLine();
}

String  bbSystemProperty( const String& p ){
	String t=gx_runtime->systemProperty( p );
	return t;
}

String   bbGetEnv( const String& env_var ){
	char *p=getenv( env_var.cstr() );
	String val=p ? p : "";
	return val;
}

void  bbSetEnv( const String& env_var,const String& val ){
	String t=env_var+"="+val;
	putenv( t.cstr() );
}

gxTimer * bbCreateTimer( int hertz ){
	gxTimer *t=gx_runtime->createTimer( hertz );
	return t;
}

int   bbWaitTimer( gxTimer *t ){
	int n=t->wait();
	return n;
}

void  bbFreeTimer( gxTimer *t ){
	gx_runtime->freeTimer( t );
}

void  _bbDebugStmt( int pos,const char *file ){
	gx_runtime->debugStmt( pos,file );
}

void  _bbDebugEnter( void *frame,void *env,const char *func ){
	gx_runtime->debugEnter( frame,env,func );
}

void  _bbDebugLeave(){
	gx_runtime->debugLeave();
}

bool basic_create();
bool basic_destroy();
bool math_create();
bool math_destroy();
bool string_create();
bool string_destroy();
bool stream_create();
bool stream_destroy();
bool sockets_create();
bool sockets_destroy();
bool filesystem_create();
bool filesystem_destroy();
bool bank_create();
bool bank_destroy();
bool graphics_create();
bool graphics_destroy();
bool input_create();
bool input_destroy();
bool audio_create();
bool audio_destroy();

bool blitz3d_create();
bool blitz3d_destroy();

//start up error
static void sue( const char *t ){
	String p=String( "Startup Error: " )+t;
	gx_runtime->debugInfo( p.cstr() );
}

bool bbruntime_create(HINSTANCE hinst){
    gx_runtime = gxRuntime::openRuntime(hinst, "");
	if( basic_create() ){
        printf("basic_create()\n");
		if( math_create() ){
            printf("math_create()\n");
			if( string_create() ){
                printf("string_create()\n");
				if( stream_create() ){
                    printf("stream_create()\n");
					if( sockets_create() ){
                        printf("sockets_create()\n");
						if( filesystem_create() ){
                            printf("filesystem_create()\n");
							if( bank_create() ){
                                printf("bank_create()\n");
								if( graphics_create() ){
                                    printf("graphics_create()\n");
									if( input_create() ){
                                        printf("input_create()\n");
										if( audio_create() ){
                                            printf("audio_create()\n");
											if( blitz3d_create() ){
                                                printf("blitz3d_create()\n");
												return true;
											}else sue( "blitz3d_create failed" );
											audio_destroy();
										}else sue( "audio_create failed" );
										input_destroy();
									}else sue( "input_create failed" );
									graphics_destroy();
								}else sue( "graphics_create failed" );
								bank_destroy();
							}else sue( "bank_create failed" );
							filesystem_destroy();
						}else sue( "filesystem_create failed" );
						sockets_destroy();
					}else sue( "sockets_create failed" );
					stream_destroy();
				}else sue( "stream_create failed" );
				string_destroy();
			}else sue( "string_create failed" );
			math_destroy();
		}else sue( "math_create failed" );
		basic_destroy();
	}else sue( "basic_create failed" );
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;
	return false;
}

bool bbruntime_destroy(){
	blitz3d_destroy();
	audio_destroy();
	input_destroy();
	graphics_destroy();
	bank_destroy();
	filesystem_destroy();
	sockets_destroy();
	stream_destroy();
	string_destroy();
	math_destroy();
	basic_destroy();
    gxRuntime::closeRuntime(gx_runtime); gx_runtime = nullptr;
	return true;
}

void bbruntime_panic( const char *err ){
	RTEX( err );
}
