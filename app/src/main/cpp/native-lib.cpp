#include <jni.h>
#include <string>
#include "Jieba.hpp"

#include <android/log.h>
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "lzh", __VA_ARGS__)

using namespace std;


const char* const DICT_PATH = "jieba.dict.utf8";
const char* const HMM_PATH = "hmm_model.utf8";
const char* const USER_DICT_PATH = "user.dict.utf8";
const char* const IDF_PATH = "idf.utf8";
const char* const STOP_WORD_PATH = "stop_words.utf8";

cppjieba::Jieba *jieba;




extern "C"
JNIEXPORT void JNICALL
Java_com_example_srct_bigbong_BigBangApplication_loadDictFromJNI(JNIEnv *env, jobject instance,
                                                                 jstring dicPath_) {
    const char *p = env->GetStringUTFChars(dicPath_, NULL);
    LOGD("Load dict start p = %s", p);
    string dict_path;
    string hmm_path;
    string user_dict_path;
    string idf_path;
    string stop_word_path;
    dict_path.append(p).append(DICT_PATH);
    hmm_path.append(p).append(HMM_PATH);
    user_dict_path.append(p).append(USER_DICT_PATH);
    idf_path.append(p).append(IDF_PATH);
    stop_word_path.append(p).append(STOP_WORD_PATH);
    jieba = new cppjieba::Jieba(dict_path.c_str(),
                          hmm_path.c_str(),
                          user_dict_path.c_str(),
                          idf_path.c_str(),
                          stop_word_path.c_str());
    LOGD("Load dict end");

    //env->ReleaseStringUTFChars(dicPath_, p);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_srct_bigbong_BigBangApplication_getKeywordFromJNI(
        JNIEnv* env,
        jobject /* this */, jstring text) {
   // const char *p = env->GetStringUTFChars(dicpath, NULL);
    const char *t = env->GetStringUTFChars(text, NULL);
    string s(t);

    //char *p2 = "/data/data/com.example.srct.myapplication/";
//    LOGD("Load dict start p = %s", p);
//    string dict_path;
//    string hmm_path;
//    string user_dict_path;
//    string idf_path;
//    string stop_word_path;
//    dict_path.append(p).append(DICT_PATH);
//    hmm_path.append(p).append(HMM_PATH);
//    user_dict_path.append(p).append(USER_DICT_PATH);
//    idf_path.append(p).append(IDF_PATH);
//    stop_word_path.append(p).append(STOP_WORD_PATH);
//    cppjieba::Jieba jieba(dict_path.c_str(),
//                          hmm_path.c_str(),
//                          user_dict_path.c_str(),
//                          idf_path.c_str(),
//                          stop_word_path.c_str());
//
//    LOGD("Load dict end");
    const size_t topk = 5;
    vector<cppjieba::KeywordExtractor::Word> keywordres;
    jieba->extractor.Extract(s, keywordres, topk);
    LOGD("keyword = %s", keywordres.at(0).word.c_str());

    std::string hello = "Hello from C++";
    return env->NewStringUTF(keywordres.at(0).word.c_str());

}






