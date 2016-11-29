#include "ByteBuffer.h"

ByteBuffer::ByteBuffer(int capacity)
{
	buffer = new char[capacity];
	position = 0;
	this->capacity = capacity;
}


ByteBuffer::~ByteBuffer()
{
	delete []buffer;
}

int ByteBuffer::remaining()
{
	return capacity - position;
}

char* ByteBuffer::getBuffer()
{
	return buffer + position;
}

void ByteBuffer::put(const char* bytes,int offset,int len)
{
	if(position + len > capacity)
	{
		printf("error -ByteBuffer::put(const char* bytes,int offset,int len)---position=%d,len=%d,capacity=%d\n",position,len,capacity);
		return;
	}
	memcpy(buffer+position,bytes+offset,len);
}


int ByteBuffer::getPosition(){
	return position;
}

void ByteBuffer::setPosition(int p){
	if(p >= capacity) {
		printf("error ByteBuffer::setPosition p> limit------------p=%d,limit=%d\n",p,capacity);
	}

	position += p;
}


int ByteBuffer::getCapacity(){
	return this->capacity;
}


void ByteBuffer::get(char* bytes,int size)
{
	get(bytes,0,size);
}

void ByteBuffer::get(char* bytes,int offset,int len)
{
	memcpy(bytes, buffer+offset+position, len);
}
/**
 * makes a buffer ready for a new sequence of channel-read or relative put operations: It sets the limit to the capacity and the position to zero.
 */
void ByteBuffer::clear()
{
	position = 0;
}

/**
 * flip() makes a buffer ready for a new sequence of channel-write or relative get operations: It sets the limit to the current position and then sets the position to zero.
 */
void ByteBuffer::flip()
{
	position = 0;
}


