#include <list>
#include <vector>
#include <cstring>
#include <pthread.h>
#include <thread>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "Includes/Utils.h"
#include "KittyMemory/MemoryPatch.h"

//Target lib here
#define targetLibName "libGameMain.so"

#include "Includes/Macros.h"
#include "HookGame/HookGame.h"
#include "HookGame/StepOne_Patch.h"


void *hack_thread(void *) {

    while (!isLibraryLoaded(targetLibName));
    StepOnePatchGame();
    Call_GetFunctionAddr();
    CallHook();

    return NULL;
}


__attribute__((constructor))
void lib_main() {
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsSeedChoosing(JNIEnv *env, jclass clazz) {
    return isSeedChoosingNow;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeNowScene(JNIEnv *env, jclass clazz) {
    return nowScene;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGameState(JNIEnv *env, jclass clazz) {
    return gameState;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsMainMenu(JNIEnv *env, jclass clazz) {
    return isMainMenu;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsDaveTalking(JNIEnv *env, jclass clazz) {
    return !isCreditScreen && isCrazyDaveShowing;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsStartButtonEnabled(JNIEnv *env, jclass clazz) {
    return isStartButtonEnabled;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsDialogExist(JNIEnv *env, jclass clazz) {
    if (isInVSSetupMenu && VSSetupState == 0) {
        return false;
    }
    return dialogCount > 0 || isAlmanacDialogExist;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsImitaterChooser(JNIEnv *env, jclass clazz) {
    return isImitaterChooser;
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsVaseBreakerMode(JNIEnv *env, jclass clazz) {
    return isVaseBreakerMode;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsCobCannonSelected(JNIEnv *env,
                                                                       jclass clazz) {
    return isCobCannonSelected;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeRequestValidCobCanonCheck(JNIEnv *env,
                                                                             jclass clazz, jint x,
                                                                             jint y) {

    col = x;
    row = y;
    isCurrentPlantValidCobCannon = false;
    requestValidCobCannonCheck = true;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetSeedBankPosition(JNIEnv *env,
                                                                       jclass clazz, jint x) {

    seekBankPositionToSet = x;
    requestSetSeedBankPosition = true;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGetSeekBankPosition(JNIEnv *env, jclass clazz) {
    return seekBankPositionOrigin;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsAwardScreen(JNIEnv *env, jclass clazz) {
    return isAwardScreen;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeMaxPlants(JNIEnv *env, jclass clazz) {
    return maxPlantsNumInSeedBank;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsSeedChooserHas7Rows(JNIEnv *env,
                                                                         jclass clazz) {
    return isSeedChooseHas7Rows;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsShovelEnabled(JNIEnv *env,
                                                                   jclass clazz) {
    return isShovelEnabled;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeEnableConveyorBeltFast(JNIEnv *env,
                                                                          jclass clazz) {
    enableConveyorBeltFast = true;
    if (UpdateConveyorBeltAddr == 0) {
        return;
    }
    MSHookFunction((void *) UpdateConveyorBeltAddr, (void *) UpdateConveyorBelt, NULL);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsSurvivalRepick(JNIEnv *env, jclass clazz) {
    return isSurvivalRepick;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetGameState(JNIEnv *env, jclass clazz,
                                                                jint state) {
    stateToSet = state;
    requestSetGameState = true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsChallengeScreen(JNIEnv *env, jclass clazz) {
    return isChallengeScreen;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetChallengeScreenScrollTarget(JNIEnv *env,
                                                                                  jclass clazz,
                                                                                  jint target,
                                                                                  jint offset) {
    scrollTarget = target;
    scrollOffset = offset;
    requestSetScrollTarget = true;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsStoreEnabled(JNIEnv *env, jclass clazz) {
    return isStoreEnabled;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsAlmanacEnabled(JNIEnv *env, jclass clazz) {
    return isAlmanacEnabled;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsImitaterEnabled(JNIEnv *env, jclass clazz) {
    return isImitaterEnabled;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsWhackAZombieLevel(JNIEnv *env, jclass clazz) {
    return isWhackAZombie;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGetCursorPositionX(JNIEnv *env, jclass clazz) {
    return gridX;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGetCursorPositionY(JNIEnv *env, jclass clazz) {
    return gridY;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeDisableShop(JNIEnv *env, jclass clazz) {
    disableShop = true;
    if (LawnApp_CanShopLevelAddr == 0) {
        return;
    }
    MSHookFunction((void *) LawnApp_CanShopLevelAddr, (void *) LawnApp_CanShopLevel, NULL);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeEnableManualCollect(JNIEnv *env, jclass clazz) {
    enableManualCollect = true;
    if (IsPatched) {
        GamePatches.ManualCollection.Modify();
        return;
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetStoreSelectedSlot(JNIEnv *env, jclass clazz,
                                                                        jint slot) {
    requestSetStoreSelectedSlot = true;
    slotToSet = slot;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsStoreScreen(JNIEnv *env, jclass clazz) {
    return isDaveStore;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGetSelectedStoreItem(JNIEnv *env, jclass clazz) {
    return selectedStoreItem;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetHeavyWeaponAngle(JNIEnv *env, jclass clazz,
                                                                       jint i) {

    float radian = i * M_PI / 180.0;
    angle1 = -cos(radian);
    angle2 = sin(radian);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetZenGardenTool(JNIEnv *env, jclass clazz,
                                                                    jint position) {
    requestSetZenGardenTool = true;
    zenGardenToolPosition = position;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeCursorType(JNIEnv *env,
                                                              jclass clazz) {
    return requestDrawShovelInCursor ? 6 : cursorType;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeHasGoldenWaterCan(JNIEnv *env, jclass clazz) {
    return isGoldWateringCan;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGameIndex(JNIEnv *env, jclass clazz) {
    return nowGameIndex;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetBoardPositionByXY(JNIEnv *env, jclass clazz,
                                                                        jfloat x, jfloat y) {
    requestSetBoardPositionByXY = true;
    boardPositionXToSet = x;
    boardPositionYToSet = y;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeCanInteractWithStoreButtons(JNIEnv *env,
                                                                               jclass clazz) {
    return canInteract;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativePickUpShovel(JNIEnv *env, jclass clazz,
                                                                jboolean pickUp) {
    requestClearCursor = true;
    requestDrawShovelInCursor = pickUp;
    requestSetGameState = pickUp;
    requestPlayFoley = true;
    foleyToPlay = 75;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeShovelDown(JNIEnv *env, jclass clazz) {
    requestShovelDown = true;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetSeedBankPosition2P(JNIEnv *env, jclass clazz,
                                                                         jint x) {
    seekBankPositionToSet_2P = x;
    requestSetSeedBankPosition_2P = true;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGetSeekBankPosition2P(JNIEnv *env,
                                                                         jclass clazz) {
    return seekBankPositionOrigin_2P;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetBoardPositionByXY2P(JNIEnv *env, jclass clazz,
                                                                          jfloat x, jfloat y) {
    requestSetBoardPositionByXY_2P = true;
    boardPositionXToSet_2P = x;
    boardPositionYToSet_2P = y;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeSetGameState2P(JNIEnv *env, jclass clazz,
                                                                  jint state) {
    stateToSet_2P = state;
    requestSetGameState_2P = true;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeGameState2P(JNIEnv *env, jclass clazz) {
    return gameState_2P;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_native1PButtonDown(JNIEnv *env, jclass clazz,
                                                                jint code) {
    requestButtonDown = true;
    buttonCode1 = code;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_native2PButtonDown(JNIEnv *env, jclass clazz,
                                                                jint code) {
    requestButtonDown_2P = true;
    buttonCode1_2P = code;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_native1PSelectPlantSeed(JNIEnv *env, jclass clazz,
                                                                     jint x, jint y) {
    requestVSPlantSelectSeed = true;
    plantX = x < 0 ? 0 : x;
    plantY = y < 0 ? 0 : y;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_transmension_mobile_EnhanceActivity_native2PSelectZombieSeed(JNIEnv *env, jclass clazz,
                                                                      jint x, jint y) {
    requestVSZombieSelectSeed = true;
    zombieX = x < 0 ? 0 : x;
    zombieY = y < 0 ? 0 : y;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeVSSetupState(JNIEnv *env, jclass clazz) {
    return isInVSSetupMenu ? VSSetupState : -1;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_transmension_mobile_EnhanceActivity_nativeIsInVSResultMenu(JNIEnv *env, jclass clazz) {
    return isInVSResultsMenu;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_android_support_Preferences_Changes(JNIEnv *env, jclass clazz, jobject con, jint featNum,
                                             jstring featName, jint value, jboolean boolean,
                                             jstring str) {

    switch (featNum) {
        case 1:
            TakeSunButt = boolean;
            break;
        
        case 1001:
            superStop = boolean;
            break;

        case 2:
            CoolDownSeedPacketButt = boolean;
            break;

        case 23:
            PassNowLevel = boolean;
            break;//过了这关

        case 50:
            RandomBulletBUTT = boolean;
            break;//随机子弹
        case 51:
            BulletSpinnerChosenNum = value - 1;
            break;

        case 52:
            ClearAllPlant = boolean;
            break;
        case 53:
            GetedChangeFormationID(value - 1);
            break;
        case 54:
            FormationDecideButt = boolean;
            break;
        case 55:
            IsOnlyPeaUseableButt = boolean;
            break;

        case 61:
            PumpkinWithLadder = boolean;
            break;

        case 62:
            LadderX = value;
            break;
        case 63:
            LadderY = value;
            break;

        case 64:
            DoneLadderBuild = boolean;
            break;
        case 65:
            ThePlantID = value - 1;
            break;
        case 66:
            ThePlantID = value;
            break;
        case 67:
            DonePlantBuild = boolean;
            break;
        case 68:
            BuildPlantX = value;
            break;
        case 69:
            BuildPlantY = value;
            break;
        case 70:
            BuildZombieCount = value;
            break;
        case 71:
            TheZombieBuildRow = value;
            break;
        case 72:
            DoneZombieBuild = boolean;
            Buildi = 0;
            break;
        case 73:
            TheZombieID = value - 1;
            break;
        case 74:
            UniformPlace = boolean;
            break;
        case 81:
            OpenGameInfoWindow = boolean;
            break;
        case 82:
            NoSunLimit = boolean;
            break;
        case 83:
            FreePlantAt = boolean;
            break;

        case 84:
            BanCobCannonBullet = boolean;
            break;
        case 85:
            BanStar = boolean;
            break;
        case 86:
            OnlyTouchFireWoodButt = boolean;
            break;
        case 87:
            ColdPeaCanPassFireWood = boolean;
            break;
        case 110:
            ZombieCanNotWon = boolean;
            break;
        case 111:
            DoCheatDialogButt = boolean;
            break;
        case 112:
            DoCheatCodeDialogButt = boolean;
            break;
        case 113:
            BanDropCoin = boolean;
            break;
        case 114:
            speedUpMode = value;
            break;
        default:
            break;

    }
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_android_support_CkHomuraMenu_GetFeatureList(JNIEnv *env, jobject thiz) {
    jobjectArray ret;
    const char *features[] = {
            "Category_By-波奇酱",

            "Collapse_常用功能",//**
            "1001_CollapseAdd_Toggle_高级暂停"
            "1_CollapseAdd_Toggle_阳光反加",
            "2_CollapseAdd_Toggle_卡片无冷却",
            "81_CollapseAdd_Toggle_出僵DEBUG",
            "82_CollapseAdd_Toggle_无阳光上限",
            "83_CollapseAdd_Toggle_自由种植",
            "110_CollapseAdd_Toggle_无视进家",
            "23_CollapseAdd_OnceCheckBox_过了这关",
            "111_CollapseAdd_OnceCheckBox_跳关器",
            "112_CollapseAdd_OnceCheckBox_作弊码",
            "114_CollapseAdd_Spinner_<font color='green'>倍速：_关闭,1.2倍,1.5倍,2倍,2.5倍,3倍,5倍,10倍",

            "Collapse_子弹功能",
            "50_CollapseAdd_Toggle_随机子弹",
            "51_CollapseAdd_Spinner_<font color='green'>固定某种子弹：_关闭,豌豆,冰豌豆,卷心菜,西瓜,泡泡,冰西瓜,焰,星星,尖刺,只因球,玉米粒,玉米炮,黄油,自残子弹",
            "86_CollapseAdd_Toggle_穿火炬生效(Pea)",
            "87_CollapseAdd_Toggle_冰豆跃火",
            "55_CollapseAdd_CheckBox_仅对豌豆生效",
            "84_CollapseAdd_CheckBox_Ban玉米加农",
            "85_CollapseAdd_CheckBox_Ban杨桃",

            "Collapse_快速布阵",
            "52_CollapseAdd_OnceCheckBox_清空当前植物",
            "53_CollapseAdd_Spinner_<font color='green'>泳池PE：_关闭,[0]电波钟无炮,[1]最简无炮,[2]伪无伤无炮,[3]自然控丑无炮,[4]火焰无炮,[5]分裂火焰无炮,[6]后退无炮,[7]超前置无炮,[8]王子无炮,[9]机械钟无炮,[10]神之无炮,[11]石英钟无炮,[12]靠天无炮,[13]方块无神无炮,[14]56加速无神无炮,[15]压制一炮,[16]小二炮,[17]火焰二炮,[18]核武二炮,[19]分裂二炮,[20]方正二炮,[21]经典二炮,[22]冲关三炮,[23]太极四炮,[24]全金属四炮,[25]方块四炮,[26]青四炮,[27]水路无植物四炮,[28]方四炮,[29]神之四炮,[30]双核底线四炮,[31]经典四炮,[32]火焰四炮,[33]底线四炮,[34]传统四炮,[35]半场无植物五炮,[36]散炸五炮,[37]心五炮,[38]陆路无植物六炮,[39]水路无植物六炮,[40]青苔六炮,[41]禅房花木深,[42]神之六炮,[43]玉米六炮,[44]空炸六炮,[45]超后置六炮,[46]方六炮,[47]蝶韵,[48]一勺汤圆,[49]间隔无植物七炮,[50]玉兔茕茕,[51]无保护八炮,[52]树八炮,[53]全对称树八炮,[54]矩形八炮,[55]神之八炮,[56]阴阳八炮,[57]浮萍八炮,[58]后置八炮,[59]饲养海豚,[60]玉米八炮,[61]经典八炮,[62]花海八炮,[63]C2八炮,[64]分离八炮,[65]全对称八炮,[66]3C八炮,[67]灯台八炮,[68]⑨炮,[69]方块九炮,[70]C6i九炮,[71]心九炮,[72]轮炸九炮,[73]②炮,[74]六芒星十炮,[75]六边形十炮,[76]方块十炮,[77]斜方十炮,[78]简化十炮,[79]后置十炮,[80]经典十炮,[81]六线囚尸,[82]斜十炮,[83]魔方十炮,[84]戴夫的小汉堡,[85]鸡尾酒,[86]一勺汤圆十二炮,[87]玉壶春十二炮,[88]半场十二炮,[89]简化十二炮,[90]经典十二炮,[91]火焰十二炮,[92]冰雨十二炮·改,[93]冰雨十二炮•改改,[94]单紫卡十二炮,[95]神柱十二炮,[96神之十二炮],[97]水路无植物十二炮,[98]纯白悬空十二炮,[99]后花园十二炮,[100]玉米十二炮,[101]两路暴狂,[102]九列十二炮,[103]梯曾十二炮,[104]君海十二炮,[105]箜篌引,[106]梅花十三,[107]最后之作,[108]冰心灯,[109]太极十四炮,[110]真·四炮,[111]神棍十四炮,[112]神之十四炮,[113]穿越十四炮,[114]钻石十五炮,[115]神之十五炮,[116]真·二炮,[117]冰箱灯,[118]炮环十二花,[119]单冰十六炮,[120]对称十六炮,[121]神之十六炮,[122]裸奔十六炮,[123]双冰十六炮,[124]超前置十六炮,[125]火焰十六炮,[126]经典十六炮,[127]折线十六炮,[128]肺十八炮(极限),[129]纯十八炮,[130]真·十八炮,[131]冰魄十八炮,[132]尾炸十八炮,[133]经典十八炮,[134]纯二十炮,[135]空炸二十炮,[136]钉耙二十炮,[137]新二十炮,[138]无冰瓜二十炮,[139]绝望之路,[140]二十一炮,[141]新二十二炮,[142]二十二炮,[143]无冰瓜二十二炮,[144]九列二十二炮,[145]二十四炮,[146]垫材二十四炮,[147]胆守(极限)",
            "54_CollapseAdd_OnceCheckBox_布阵",

            "Collapse_快速搭梯",
            "61_CollapseAdd_Toggle_梯子南瓜",
            "62_CollapseAdd_InputValue_X坐标",
            "63_CollapseAdd_InputValue_Y坐标",
            "64_CollapseAdd_OnceCheckBox_搭建",

            "Collapse_植物&僵尸建造(测压)",
            "CollapseAdd_RichTextView_<font color='yellow'>僵尸:--->",
            "73_CollapseAdd_Spinner_<font color='green'>选择出现僵尸：_关闭,普通,旗帜,路障,撑杆,铁桶,读报,铁门,橄榄球,舞王,舞伴,游池普僵,潜水,冰车,雪橇,海豚,小丑,气球,矿工,蹦蹦,雪人,飞贼,梯子,篮球,巨人,小鬼,僵王,垃圾桶,豌豆,坚果,辣椒,机枪,倭瓜,高坚果,红眼巨人",
            "70_CollapseAdd_InputValue_出现数量",
            "71_CollapseAdd_InputValue_出现行数",
            "74_CollapseAdd_CheckBox_均匀放置",
            "72_CollapseAdd_OnceCheckBox_种植",
            "CollapseAdd_RichTextView_<font color='yellow'>植物:--->",
            "65_CollapseAdd_Spinner_<font color='green'>选择建造植物：_关闭,豌豆射手,向日葵,樱桃炸弹,坚果,土豆地雷,寒冰射手,食人花,双发射手,小喷菇,阳光菇,大喷菇,咬咬碑,迷惑菇,胆小菇,冰冻菇,毁灭菇,睡莲,窝瓜,三线射手,海藻,火爆辣椒,地刺,火炬树桩,高坚果,海蘑菇,灯笼草,仙人掌,三叶草,双向射手,杨桃,南瓜套,磁力菇,卷心菜,花盆,玉米投手,咖啡豆,大蒜,叶子保护伞,金盏花,西瓜投手,机枪射手,双子向日葵,忧郁蘑菇,猫尾草,冰冻西瓜,吸金瓷,地刺王,玉米加农炮,模仿者,爆炸坚果,巨型坚果,数苗,反向双发射手",
            "66_CollapseAdd_InputValue_自定义种植ID",
            "68_CollapseAdd_InputValue_X坐标",
            "69_CollapseAdd_InputValue_Y坐标",
            "67_CollapseAdd_OnceCheckBox_种植",
            "CollapseAdd_RichTextView_<font color='yellow'>#可以暂停游戏多次使用",
            "CollapseAdd_RichTextView_<font color='yellow'>#可用于针对测压",

            "Collapse_掉落收集",
            "113_CollapseAdd_Toggle_禁止掉落阳光金币",


    };

    int Total_Feature = (sizeof features / sizeof features[0]);
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass("java/lang/String"),
                                env->NewStringUTF(""));

    for (int i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    return (ret);

}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_android_support_CkHomuraMenu_SettingsList(JNIEnv *env, jobject thiz) {
    jobjectArray ret;
    const char *features[] = {

            OBFUSCATE("Category_设置"),
            OBFUSCATE("-1_Toggle_保存功能首选项"), //-1 is checked on Preferences.java
            OBFUSCATE("Category_选择"),
            OBFUSCATE("-6_Button_<font color='red'>退出设置</font>"),
    };

    int Total_Feature = (sizeof features / sizeof features[0]);
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass(OBFUSCATE("java/lang/String")),
                                env->NewStringUTF(""));
    int i;
    for (i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    return (ret);

}













