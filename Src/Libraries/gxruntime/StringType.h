#ifndef STRING_H_INCLUDED
#define STRING_H_INCLUDED

#include <string>
#include <iostream>
#include <vector>

typedef wchar_t wchar;

struct String {
    public:
        ~String();
        String();
        String(const String& a);
        String(const char* cstr);
        String(const std::string& cppstr);
        String(const wchar* wstr);
        String(const std::wstring& cppwstr);
        String(const String& a,const String& b);
        String(char c);
        String(wchar w);
        String(int i);
        String(float f);

        const char* cstr() const;
        const wchar* wstr() const;
        int toInt() const;
        float toFloat() const;

        int size() const;

        int findFirst(const String& fnd,int from=-1) const;
        int findLast(const String& fnd,int from=-1) const;

        String substr(int start,int cnt=-1) const;
        char charAt(int pos) const;
        String replace(const String& fnd,const String& rplace) const;
        String toUpper() const;
        String toLower() const;
        String trim() const;
        String unHex() const;
        String resourcePath() const;

        String& operator=(const String& other);

        bool equals(const String& other) const;
        bool equals(const char* other) const;
        bool equals(char other) const;
        bool isEmpty() const;

		std::vector<String> split(const String& needle, bool removeEmptyEntries = false) const;
    protected:
        enum class DOMINANT_BUFFER {
            C, W
        } dominantBuffer;

        char* cbuffer = nullptr;
        wchar* wbuffer = nullptr;
        int capacity = 16;
        int strSize = 0;

        void syncBuffers();
};

String operator+(const String& a, const String& b);
String operator+(const char* a, const String& b);
std::ostream& operator<<(std::ostream &os, const String& s);

#endif
