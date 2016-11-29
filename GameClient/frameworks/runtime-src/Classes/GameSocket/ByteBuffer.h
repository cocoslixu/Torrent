
#ifndef BYTE_BUFFER_H
#define BYTE_BUFFER_H

#include "cocos2d.h"
USING_NS_CC;

class ByteBuffer 
{
public:
	char* buffer;
	int position;
	int capacity;
public:
	
	ByteBuffer(int capacity);

	~ByteBuffer();
	
	int remaining();

	void put(const char* bytes,int offset,int len);

	int getPosition();

	void setPosition(int p);

	int getCapacity();

	char* getBuffer();

	void get(char* bytes,int size);

	void get(char* bytes,int offset,int len);

    void clear();

    int getLength(int offset);

    void flip();
};


#endif
