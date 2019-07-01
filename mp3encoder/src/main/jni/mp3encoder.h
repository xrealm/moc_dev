//
// Created by Mao on 2/6/18.
//

#include <cstdio>
#include "include/lame/lame.h"

#ifndef MP3ENCODER_MP3ENCODER_H
#define MP3ENCODER_MP3ENCODER_H

#endif //MP3ENCODER_MP3ENCODER_H

class Mp3Encoder {
private:
    FILE* pcmFile;
    FILE* mp3File;
    lame_t lameClient;

public:
    Mp3Encoder();
    ~Mp3Encoder();
    int Init(const char* pcmFilePath, const char* mp3FilePath, int sampleRate, int channels, int bitRate);
    void Encode();
    void Destory();
};