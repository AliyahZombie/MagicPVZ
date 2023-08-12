#ifndef PVZ_TV_1_1_5_HOOKGAME_H
#define PVZ_TV_1_1_5_HOOKGAME_H

#include "HookGame/HookAddr.h"
#include "ChangeFormation.h"
#include "StepOne_Patch.h"
#include <Substrate/SubstrateHook.h>
#include <Substrate/CydiaSubstrate.h>
#include <cstdlib>
#include <ctime>


#define targetLibName "libGameMain.so"

typedef unsigned int _DWORD;
typedef unsigned char _BYTE;
//***********
int nowScene = -1;  //当前的场景，白天、黑夜、泳池、雾夜、屋顶、月夜、花园、蘑菇园
int gameState = 1;  //当前是是否正在SeedBank中选择植物种子。其可能的值为：    1：手上没拿种子，也不在SeedBank里选种子     6：正在SeedBank里选种子(我是僵尸模式也会生效哦)     7：已经选择完毕，手上正拿着种子
int dialogCount = 0; //当前对话框数量。如果这个数字大于0，则说明有对话框。
int maxPlantsNumInSeedBank = 0;  //SeedBank中的植物卡片数量
int nowGameIndex = 0; //小游戏索引
int VSSetupState = 0;//对战状态
bool isMainMenu = true; //游戏是否在主界面
bool isSeedChooseHas7Rows = false;//玩家在选择种子界面是否拥有7行的植物
bool isSeedChoosingNow = false;//游戏是否在选择种子界面
bool isInVSSetupMenu = false;//游戏是否在对战开始前的选择种子界面
bool isImitaterEnabled = false;//玩家是否拥有模仿者植物
bool isStoreEnabled = false;//玩家是否拥有商店
bool isAlmanacEnabled = false;//玩家是否拥有图鉴
bool isImitaterChooser = false;//游戏是否在模仿者选择种子界面
bool isStartButtonEnabled = false;//选择种子界面的“开始按钮”是否可以点击（即种子是否选满了）
bool isAwardScreen = false; //游戏是否在关卡完成时的奖励界面
bool isCrazyDaveShowing = false;//是否存在疯狂戴夫正与你对话
bool isVaseBreakerMode = false;//当前游戏是否为砸罐子模式
bool isWhackAZombie = false;//游戏是否在砸僵尸模式
bool isShovelEnabled = false;//当前关卡是否拥有铲子
bool isAlmanacDialogExist = false;//游戏是否在图鉴界面
bool isDaveStore = false;//游戏是否在商店界面
bool isSurvivalRepick = false;//游戏是否为生存模式打完第一面旗帜之后的带有“查看草坪”按钮的选卡界面
bool isCreditScreen = false;//是否在制作人员界面
bool isChallengeScreen = false;//游戏是否在小游戏列表界面
bool isInShovelTutorial = false;//冒险模式1-5开头的铲子教学模式
int gridX = 0;//Board光标横坐标
int gridY = 0;//Board光标纵坐标

int gameState_2P = 1;
int gridX_2P = 0;//Board光标横坐标
int gridY_2P = 0;//Board光标纵坐标

//用于检查（row,col）处是否为一个充能完毕的加农炮
bool requestValidCobCannonCheck = false;
bool isCurrentPlantValidCobCannon = false;
bool isCobCannonSelected = false;
bool isCobCannonSelected_2P = false;
int row = 0;
int col = 0;


//用于拿起加农炮
bool requestCobCannonPickUp = false;
bool requestCobCannonPickUp_2P = false;
int cobCannonPlant = 0;


//用于设定SeedBank中的光标位置
int seekBankPositionOrigin = 0;
int seekBankPositionToSet = 0;
bool requestSetSeedBankPosition = false;
int seekBankPositionOrigin_2P = 0;
int seekBankPositionToSet_2P = 0;
bool requestSetSeedBankPosition_2P = false;


//用于设定Board中的光标位置
float boardPositionXToSet = 80;
float boardPositionYToSet = 130;
bool requestSetBoardPositionByXY = false;
float boardPositionXToSet_2P = 80;
float boardPositionYToSet_2P = 130;
bool requestSetBoardPositionByXY_2P = false;


//用于native层发送按键
bool requestButtonDown = false;
int buttonCode1 = 0;
bool requestButtonDown_2P = false;
int buttonCode1_2P = 0;


//用于设定选卡状态
bool requestSetGameState = false;
int stateToSet = 0;
bool requestSetGameState_2P = false;
int stateToSet_2P = 0;

//用于小游戏界面滚动
int scrollTarget = 0;
int scrollOffset = 0;
bool requestSetScrollTarget = false;
bool requestNewGame = false;

//用于商店界面的触控
bool requestSetStoreSelectedSlot = false;
bool canInteract = false;
int slotToSet = 0;
int selectedStoreItem = 0;


//重型武器角度设定
float angle1 = 0;
float angle2 = 1;


//铲子
bool requestShovelDown = false;
bool requestClearCursor = false;
bool requestPlayFoley = false;
bool requestDrawShovelInCursor = false;
int plantUnderShovel = 0;
int foleyToPlay = 8;


//花园工具类型
int cursorType = 0;

// advanced option for magicpvz
bool superStop = false;


//花园触控
bool requestSetZenGardenTool = false;
int zenGardenToolPosition = 0;
int zenGardenObjects[11] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
int zenGardenObjectsCount = 0;
bool isGoldWateringCan = false;

//检查加农炮用
int (*Board_GetPlantsOnLawn)(int result, unsigned int a2, unsigned int a3, int *a4);

bool (*SeedChooserScreeen_Has7Rows)(void *instance);

bool (*CutScene_IsSurvivalRepick)(void *instance);

int (*ChallengeScreen_SetScrollTarget)(int *instance, int a);

int (*Board_GridToPixelX)(void *result, unsigned int a2, unsigned int a3);

int (*Board_GridToPixelY)(void *result, unsigned int a2, unsigned int a3);

int (*Board_PixelToGridX)(void *result, unsigned int a2, unsigned int a3);

int (*Board_PixelToGridY)(void *result, unsigned int a2, unsigned int a3);

int (*Board_ToolHitTest)(int *a1, int a2, int a3);

int (*Board_UpdateGame)(void *a);

int (*Board_NewPlant)(int *a1, int a2, int a3, int a4, int a5, int a6);

int (*Board_GetTopPlantAt)(int *a1, unsigned int a2, unsigned int a3, int a4);

int (*LawnApp_KillChallengeScreen)(int a);

int (*LawnApp_KillMainMenu)(int a);

int (*LawnApp_KillNewOptionsDialog)(int a);

int (*LawnApp_PreNewGame)(int a, int a2, int a3);

int (*LawnApp_CanShowStore)(int a);

int (*LawnApp_CanShowAlmanac)(int a);

int (*LawnApp_IsScaryPotterLevel)(int *a);

int (*LawnApp_IsWhackAZombieLevel)(int *a);

int (*SeedChooserScreen_HasPacket)(int *a, int a2, bool a3);

int (*Coin_Die)(int a);

int (*GamepadControls_GetSeedBank)(int a);

int (*Board_ClearCursor)(int *a1, int a2);

#define randomInt(a, b) (rand()%(b-a+1)+a)//指定范围随机数产生

//-----------------------------------------------
//游戏函数
unsigned int (*AddALadder)(void *instance, int x, int y);

int (*AddPlant)(void *instance, int x, int y, int SeedType, int theImitaterType);

int (*AddZombieInRow)(void *instance, int theZombieType, int theRow, int theFromWave);

int (*Plant_Die)(void *instance);

int (*StoreScreen_SetSelectedSlot)(int a1, int a2);

int (*Board_CanUseGameObject)(int *a1, int a2);

int (*LawnApp_PlayFoley)(int _this, int theFoleyType);

int (*Attachment_AttachmentDie)(int result);

int (*ZOMBIE_DieNoLoot)(int Zombie_this);

int (*ZOMBIE_ApplyBurn)(int Zombie_this);

int (*DoCheatDialog)(int LawnApp_this);

int (*DoCheatCodeDialog)(int LawnApp_this);

int (*LawnApp_ReanimationTryToGet)(int a1, int a2);

int (*GamepadControls_OnKeyDown)(int a1, int a2, unsigned int a3);

int (*GamepadControls_OnButtonDown)(int a1, int a2, int a3, unsigned int a4);

void (*Board_DisplayAdvice)(int* , std::string const&, uint, uint16_t);

// 确定 13 1096
// 返回 27 1096

// 左 37 1096
// 上 38 1096
// 右 39 1096
// 下 40 1096

// 铲子 49 1112
// 铲子 50 1112


void GamepadControls_pickUpCobCannon(int gamePad, int cobCannon) {
    int v8; // r6
    int v9; // r1
    int *v10; // r0
    int v11; // r3
    int v12; // r3
    int v13; // r7
    int v14; // r6
    int v15; // r1
    int v16; // s14
    int v17; // s15
    _DWORD *v18; // r3
    int v19; // r3

    v8 = 0;
    v9 = *((_DWORD *) gamePad + 37);
    v10 = (int *) *((_DWORD *) gamePad + 14);
    if (v9)
        v11 = 140;
    else
        v11 = 141;
    v12 = *((_DWORD *) v10 + v11);
    if (*(_DWORD *) (v12 + 152) != -1) {
        if (*(_BYTE *) (v12 + 200)) {
            v19 = *(_DWORD *) (v12 + 196);
            if (v19) {
                if ((unsigned int) (unsigned int) v19 < *((_DWORD *) v10 + 79)) {
                    if (v19 ==
                        *(_DWORD *) (*((_DWORD *) v10 + 77) + 352 * (unsigned int) v19 + 348))
                        v8 = *((_DWORD *) v10 + 77) + 352 * (unsigned int) v19;
                    else
                        v8 = 0;
                }
            } else {
                v8 = 0;
            }
        } else {
            v8 = *(_BYTE *) (v12 + 200);
        }
    }
    v13 = cobCannon;
    if (cobCannon != v8 && *(_DWORD *) (cobCannon + 76) == 37 && *((_DWORD *) gamePad + 24) != 8) {
        v14 = *((_BYTE *) gamePad + 200);
        if (!*((_BYTE *) gamePad + 200)) {
            Board_ClearCursor(v10, v9);
            v15 = *(_DWORD *) (v13 + 348);
            v16 = (int) *((float *) gamePad + 27);
            v17 = (int) *((float *) gamePad + 28);
            v18 = (_DWORD *) (*((_DWORD *) gamePad + 14) + 22528);
            v18[29] = 30;
            v18[30] = v16;
            v18[31] = v17;
            *((_DWORD *) gamePad + 49) = v15;
            *((_DWORD *) gamePad + 55) = v14;
            *((_BYTE *) gamePad + 200) = 1;
        }
    }
}


bool FormationButt;
int FormationBuild_x = 0;
int FormationBuild_y = 0;
bool IsRoofButt;
bool ClearAllPlant;
bool FormationDecideButt;
int ChangeFormationArr[30][9] = {{1314}};//布阵植物数组
//布阵建造普通植物韩束
void FormationBuildPlant(void *FormationBtn, int PlantArr[30][9]) {

    if (PlantArr[FormationBuild_y + 12][FormationBuild_x] != -1) {
        AddPlant(FormationBtn, FormationBuild_x, FormationBuild_y,
                 PlantArr[FormationBuild_y + 12][FormationBuild_x], 0);//荷叶建造
    }//建荷叶
    if (IsRoofButt && FormationBuild_y != 5) {
        AddPlant(FormationBtn, FormationBuild_x, FormationBuild_y, 33, 0);//花盆建造
    }//建造花盆
    if (PlantArr[FormationBuild_y][FormationBuild_x] != -1) {
        AddPlant(FormationBtn, FormationBuild_x, FormationBuild_y,
                 PlantArr[FormationBuild_y][FormationBuild_x], 0);//植物建造
    }//建普通植物
    if (PlantArr[FormationBuild_y + 6][FormationBuild_x] != -1) {
        AddPlant(FormationBtn, FormationBuild_x, FormationBuild_y,
                 PlantArr[FormationBuild_y + 6][FormationBuild_x], 0);//南瓜建造
    }//建南瓜

    if (PlantArr[FormationBuild_y + 18][FormationBuild_x] != -1) {
        AddPlant(FormationBtn, FormationBuild_x, FormationBuild_y, 35, 0);//咖啡豆建造
    }//建咖啡豆
    if (PlantArr[FormationBuild_y + 24][FormationBuild_x] != -1) {
        AddALadder(FormationBtn, FormationBuild_x, FormationBuild_y);//咖啡豆建造
    }//建梯子




    FormationBuild_x++;
    if (FormationBuild_x >= 9) {
        FormationBuild_y++;
        FormationBuild_x = 0;
    }
    if (FormationBuild_y > 5) {
        FormationButt = 0;
        IsRoofButt = 0;
        FormationDecideButt = 0;
        ChangeFormationArr[0][0] = 1314;
        FormationBuild_x = 0;
        FormationBuild_y = 0;

    }


}//

//Board
int LadderX = 0;
int BuildPlantX = 0;
int BuildZombieX = 0;
int LadderY = 0;
int BuildPlantY = 0;
int BuildZombieY = 0;
bool DoneLadderBuild;
bool DonePlantBuild;
bool DoneZombieBuild;
void *Board_instanceBtn;
int ThePlantID = 0;
int TheZombieID = 0;
int BuildZombieCount = 1;
int TheZombieBuildRow = 0;
int Buildi = 0;
bool UniformPlace;
int UniformRow = 0;
int NowPlantCount = 0;//Board植物数
bool PassNowLevel;
bool OpenGameInfoWindow;
void *FormationBtn;
int speedUpMode = 0;
int speedUpCounter = 0;


bool requestQuickDig = false;

void Board_ShovelDown(int *instance) {
//    int plant = Board_ToolHitTest(instance, x, y);
    if (isInShovelTutorial) {
        requestQuickDig = true;
    } else if (plantUnderShovel) {
        requestPlayFoley = true;
        foleyToPlay = 8;
        Plant_Die((int *) plantUnderShovel);
        if (*((_DWORD *) plantUnderShovel + 13) == 43
            && Board_GetTopPlantAt(instance, *((_DWORD *) plantUnderShovel + 14),
                                   *((_DWORD *) plantUnderShovel + 11), 8)) {
            Board_NewPlant(instance, *((_DWORD *) plantUnderShovel + 14),
                           *((_DWORD *) plantUnderShovel + 11), 16, -1, -1);
        }
    }
    plantUnderShovel = 0;
    Board_ClearCursor(instance, 0);
    requestDrawShovelInCursor = false;
}


void Reanimation_OverrideScale(int ream, float a2, float a3) {
    int v3; // r3
    v3 = *(_DWORD *) (ream + 188);
    *(_BYTE *) (ream + 208) = 0;
    *(float *) (ream + 52) = a2;
    *(float *) (ream + 68) = a3;
    *(_DWORD *) (ream + 188) = v3 | 2;
}

int (*old_Board_Update)(int *instance);
int *BOARDINSTANCE;

int Board_Update(int *instance) {
    if(superStop) return;
    if (instance != NULL) {
        BOARDINSTANCE = instance;
        isMainMenu = false;
        isInVSSetupMenu = false;
        nowScene = *(int *) ((uintptr_t) instance + 0x5660);
        int cursorObject_1P = *((_DWORD *) instance + 142);
        if (requestSetZenGardenTool) {
            requestSetZenGardenTool = false;
            if (zenGardenToolPosition < zenGardenObjectsCount) {
                *(_DWORD *) (cursorObject_1P + 64) = zenGardenObjects[zenGardenToolPosition];
            }
        }
        cursorType = *(_DWORD *) (cursorObject_1P + 64);

//        __android_log_print(ANDROID_LOG_DEBUG, "TAG", "%d", cursorType);
        if (requestShovelDown) {
            Board_ShovelDown(instance);
            requestShovelDown = false;
        }
        if (requestClearCursor) {
            Board_ClearCursor(instance, 0);
            plantUnderShovel = 0;
            requestClearCursor = false;
        }
        if (requestDrawShovelInCursor) {
            plantUnderShovel = Board_ToolHitTest(instance, boardPositionXToSet,
                                                 boardPositionYToSet);
        }
        if (requestValidCobCannonCheck) {
            int plants[4] = {0};
            Board_GetPlantsOnLawn((uintptr_t) instance, col, row, plants);
            isCurrentPlantValidCobCannon = plants[3] != 0 && *((int *) plants[3] + 13) == 47 &&
                                           *((int *) plants[3] + 19) == 37;
            requestValidCobCannonCheck = false;
            if (isCurrentPlantValidCobCannon) {
                requestSetGameState = true;
                stateToSet = 1;
//                requestButtonDown = true;
//                buttonCode1 = 13;
//                buttonCode2 = 1096;
                requestCobCannonPickUp = true;
                cobCannonPlant = plants[3];
            }
        }
        int gamePad = *((_DWORD *) instance + 140);
        int gamePad_2P = *((_DWORD *) instance + 141);
        if (gamePad != 0) {
            *(_DWORD *) (gamePad + 152) = 0;
            isCobCannonSelected = *((_BYTE *) gamePad + 200);
            if (requestSetSeedBankPosition) {
                *(int *) (gamePad + 0x60) = requestDrawShovelInCursor ? 1 : 7;
                *((int *) gamePad + 45) = seekBankPositionToSet;
                int v7 = GamepadControls_GetSeedBank(gamePad);
                int result = v7 + 116 * seekBankPositionToSet;
                *(_DWORD *) (result + 172) = 0;
                requestSetSeedBankPosition = false;
            }
            if (requestSetGameState) {
                *(int *) (gamePad + 0x60) = requestDrawShovelInCursor ? 1 : stateToSet;
                requestSetGameState = false;
            }
            gameState = *(int *) (gamePad + 0x60);
            seekBankPositionOrigin = *((int *) gamePad + 45);
            if (requestSetBoardPositionByXY) {
                *(float *) (gamePad + 108) = boardPositionXToSet;
                *(float *) (gamePad + 112) = boardPositionYToSet;
                requestSetBoardPositionByXY = false;
            }
            gridX = Board_PixelToGridX(instance, *(float *) (gamePad + 108),
                                       *(float *) (gamePad + 112));
            gridY = Board_PixelToGridY(instance, *(float *) (gamePad + 108),
                                       *(float *) (gamePad + 112));
            if (requestQuickDig) {
                GamepadControls_OnKeyDown(gamePad, 49, 1112);
                requestQuickDig = false;
            }
            if (requestButtonDown) {
                GamepadControls_OnButtonDown(gamePad, buttonCode1, 0, 0);
                requestButtonDown = false;
            }
            if (requestCobCannonPickUp) {
                GamepadControls_pickUpCobCannon(gamePad, cobCannonPlant);
                requestCobCannonPickUp = false;
            }
        }
        if (gamePad_2P != 0 && nowGameIndex == 76) {
            *(_DWORD *) (gamePad_2P + 152) = 1;
            isCobCannonSelected_2P = *((_BYTE *) gamePad_2P + 200);
            if (requestSetSeedBankPosition_2P) {
                *(int *) (gamePad_2P + 0x60) = 7;
                *((int *) gamePad_2P + 45) = seekBankPositionToSet_2P;
                int v7 = GamepadControls_GetSeedBank(gamePad_2P);
                int result = v7 + 116 * seekBankPositionToSet_2P;
                *(_DWORD *) (result + 172) = 0;
                requestSetSeedBankPosition_2P = false;
            }
            if (requestSetGameState_2P) {
                *(int *) (gamePad_2P + 0x60) = stateToSet_2P;
                requestSetGameState_2P = false;
            }
            gameState_2P = *(int *) (gamePad_2P + 0x60);
            seekBankPositionOrigin_2P = *((int *) gamePad_2P + 45);
            if (requestSetBoardPositionByXY_2P) {
                *(float *) (gamePad_2P + 108) = boardPositionXToSet_2P;
                *(float *) (gamePad_2P + 112) = boardPositionYToSet_2P;
                requestSetBoardPositionByXY_2P = false;
            }
            gridX_2P = Board_PixelToGridX(instance, *(float *) (gamePad_2P + 108),
                                          *(float *) (gamePad_2P + 112));
            gridY_2P = Board_PixelToGridY(instance, *(float *) (gamePad_2P + 108),
                                          *(float *) (gamePad_2P + 112));
            if (requestButtonDown_2P) {
                //GamepadControls_OnKeyDown(gamePad_2P, buttonCode1_2P, buttonCode2_2P);
                GamepadControls_OnButtonDown(gamePad_2P, buttonCode1_2P, 1, 0);
                *(int *) (gamePad_2P + 0x60) = 1;
                requestButtonDown_2P = false;
            }
            if (requestCobCannonPickUp_2P) {
                GamepadControls_pickUpCobCannon(gamePad_2P, cobCannonPlant);
                requestCobCannonPickUp_2P = false;
            }
        }
        if (!*((_BYTE *) instance + 601) && *((int *) instance + 5658) <= 0) {
            switch (speedUpMode) {
                case 1:
                    if (speedUpCounter++ % 5 == 0)
                        Board_UpdateGame(instance);
                    break;
                case 2:
                    if (speedUpCounter++ % 2 == 0)
                        Board_UpdateGame(instance);
                    break;
                case 3:
                    Board_UpdateGame(instance);
                    break;
                case 4:
                    Board_UpdateGame(instance);
                    if (speedUpCounter++ % 2 == 0)
                        Board_UpdateGame(instance);
                    break;
                case 5:
                    for (int i = 0; i < 2; ++i)
                        Board_UpdateGame(instance);
                    break;
                case 6:
                    for (int i = 0; i < 4; ++i)
                        Board_UpdateGame(instance);
                    break;
                case 7:
                    for (int i = 0; i < 9; ++i)
                        Board_UpdateGame(instance);
                    break;
                default:
                    break;
            }
        }
        //----------*************------------
        NowPlantCount = *(int *) ((uintptr_t) instance + 0x144);
        if (ClearAllPlant && NowPlantCount == 0) {
            ClearAllPlant = false;
        }

        if (PassNowLevel) {
            *(int *) ((uintptr_t) instance + 0x571C) = 1;
            PassNowLevel = false;
        }

        if (OpenGameInfoWindow) {
            *(_BYTE *) ((uintptr_t) instance + 0x5718) = 1;
        } else {
            *(_BYTE *) ((uintptr_t) instance + 0x5718) = 0;
        }


        //植物建造----------------
        if (FormationButt && FormationDecideButt) {
            if (ChangeFormationArr[0][0] != 1314) {
                FormationBuildPlant(instance, ChangeFormationArr);
            }
        }
        //-------------------------
        if (DoneLadderBuild) {
            AddALadder(instance, LadderX, LadderY);
            DoneLadderBuild = 0;
        }

        if (DonePlantBuild && ThePlantID != -1) {
            AddPlant(instance, BuildPlantX, BuildPlantY, ThePlantID, 0);
            DonePlantBuild = 0;
        }

        if (DoneZombieBuild && TheZombieID != -1) {
            if (Buildi >= BuildZombieCount) {
                DoneZombieBuild = 0;
                return old_Board_Update(instance);
            }
            if (UniformPlace) {
                AddZombieInRow(instance, TheZombieID, UniformRow, 0);
                UniformRow++;
                Buildi++;
                if (UniformRow > ((nowScene == 2 || nowScene == 3) ? 5 : 4)) {
                    UniformRow = 0;
                }
            } else {
                AddZombieInRow(instance, TheZombieID, TheZombieBuildRow, 0);
                Buildi++;
            }

        }
        //-------------------------

    }

    return old_Board_Update(instance);
}


int (*old_MainMenu_Update)(void *instance);

int MainMenu_Update(void *instance) {
    if (instance != NULL) {
        isMainMenu = true;
        isSurvivalRepick = false;
        //  mainMenuScene = *((int *) instance + 75);
        isSeedChoosingNow = false;
        isInVSSetupMenu = false;
    }
    return old_MainMenu_Update(instance);
}

int (*old_SeedChooserScreen_EnableStartButton)(void *instance, int isEnabled);

int SeedChooserScreen_EnableStartButton(void *instance, int isEnabled) {
    if (instance != NULL) {
        isStartButtonEnabled = isEnabled;
        isSeedChooseHas7Rows = SeedChooserScreeen_Has7Rows(instance);
    }
    return old_SeedChooserScreen_EnableStartButton(instance, isEnabled);
}


int (*old_GamepadControls_ButtonDownFireCobcannonTest)(int *instance);

int GamepadControls_ButtonDownFireCobcannonTest(int *instance) {
    int *v5 = (int *) *((int *) instance + 14);
    *((int *) v5 + 5661) = 0;
    return old_GamepadControls_ButtonDownFireCobcannonTest(instance);
}


int (*old_CutScene_Update)(void *instance);

int CutScene_Update(void *instance) {
    if (instance != NULL) {
        isSeedChoosingNow = *(bool *) ((uintptr_t) instance + 44);
        if (!isSeedChoosingNow) {
            isStartButtonEnabled = false;
            isSurvivalRepick = false;
        }
        int v2 = *((int *) instance + 8);
        isCrazyDaveShowing = *(int *) (*(int *) instance + 2308) == 2 && (v2 > 0) &&
                             *((int *) instance + 2) < v2 + 3500;
        isShovelEnabled = *(_BYTE *) (*((_DWORD *) instance + 1) + 22289);
        isInShovelTutorial =
                (unsigned int) (*(_DWORD *) (*((_DWORD *) instance + 1) + 22176) - 15) <= 2;
    }
    return old_CutScene_Update(instance);
}


int (*old_LawnApp_ShowAwardScreen)(int a1, int a2);

int LawnApp_ShowAwardScreen(int a1, int a2) {
    if (a1 != NULL) {
        isAwardScreen = true;
    }
    return old_LawnApp_ShowAwardScreen(a1, a2);
}

int (*old_LawnApp_KillAwardScreen)(int a1);

int LawnApp_KillAwardScreen(int a1) {
    if (a1 != NULL) {
        isAwardScreen = false;
    }
    return old_LawnApp_KillAwardScreen(a1);
}


int (*old_SexyDialog_AddedToManager)(void *instance, void *instance1);

int SexyDialog_AddedToManager(void *instance, void *instance1) {
    if (instance != NULL) {
        dialogCount++;
    }
    return old_SexyDialog_AddedToManager(instance, instance1);
}

int (*old_SexyDialog_RemovedFromManager)(void *instance, void *instance1);

int SexyDialog_RemovedFromManager(void *instance, void *instance1) {
    if (instance != NULL) {
        dialogCount--;
        isImitaterChooser = false;
    }
    return old_SexyDialog_RemovedFromManager(instance, instance1);
}


int (*old_ImitaterDialog_ImitaterDialog)(void *instance, int a2);

int ImitaterDialog_ImitaterDialog(void *instance, int a2) {
    if (instance != NULL) {
        isImitaterChooser = true;
    }
    return old_ImitaterDialog_ImitaterDialog(instance, a2);
}


int (*old_AlmanacDialog_AddedToManager)(void *instance, int a2);

int AlmanacDialog_AddedToManager(int *a, int a2) {
    isAlmanacDialogExist = true;
    return old_AlmanacDialog_AddedToManager(a, a2);

}


int (*old_AlmanacDialog_RemovedFromManager)(void *instance, int a2);

int AlmanacDialog_RemovedFromManager(int *a, int a2) {
    isAlmanacDialogExist = false;
    dialogCount++;
    return old_AlmanacDialog_RemovedFromManager(a, a2);

}

int (*old_StoreScreen_AddedToManager)(int *a, int a2);

int StoreScreen_AddedToManager(int *a, int a2) {
    isDaveStore = true;
    isMainMenu = false;
    return old_StoreScreen_AddedToManager(a, a2);
}


int (*old_StoreScreen_RemovedFromManager)(int *a, int a2);

int StoreScreen_RemovedFromManager(int *a, int a2) {
    isDaveStore = false;
    return old_StoreScreen_RemovedFromManager(a, a2);
}

//菜单DoCheatDialog
bool DoCheatDialogButt = false;
//菜单DoCheatCodeDialog
bool DoCheatCodeDialogButt = false;


int (*old_LawnApp_UpDateApp)(int *a);

int LawnApp_UpDateApp(int *a) {
    if (a != NULL) {
        nowGameIndex = *((int *) a + 576);
        isVaseBreakerMode = LawnApp_IsScaryPotterLevel(a);
        isWhackAZombie = LawnApp_IsWhackAZombieLevel(a);
        //_________&&&&&&******_________
        if (DoCheatDialogButt) {
            if (!isMainMenu && !isChallengeScreen) DoCheatDialog((uintptr_t) a);
            DoCheatDialogButt = false;
        }
        if (DoCheatCodeDialogButt) {
            if (!isMainMenu && !isChallengeScreen) DoCheatCodeDialog((uintptr_t) a);
            DoCheatCodeDialogButt = false;
        }
        if (requestPlayFoley) {
            LawnApp_PlayFoley((uintptr_t) a, foleyToPlay);
            requestPlayFoley = false;
        }


    }
    return old_LawnApp_UpDateApp(a);
}


bool TakeSunButt;//阳光反加开关
int (*old_Board_TakeSunMoney)(void *instance, int theAmount, int a3);

int Board_TakeSunMoney(void *instance, int theAmount, int a3) {

    if (instance != NULL) {
        if (TakeSunButt) {
            theAmount = -theAmount;
        }
    }
    return old_Board_TakeSunMoney(instance, theAmount, a3);
}

bool CoolDownSeedPacketButt;//选卡后冷却开关
void (*old_SeedPacket_Update)(int *_this);

void SeedPacket_Update(int *_this) {

    if (_this != NULL) {
        if (CoolDownSeedPacketButt) {
            *(_BYTE *) ((uintptr_t) _this + 88) = 1;//mActive
            *(_BYTE *) ((uintptr_t) _this + 89) = 0;
        }
    }
    return old_SeedPacket_Update(_this);
}

int (*old_Plant_GetRefreshTimeSeed)(int theSeedType, int theImitaterType);

int Plant_GetRefreshTimeSeed(int theSeedType, int theImitaterType) {

    if (theSeedType != NULL) {
        if (CoolDownSeedPacketButt) {
            return 0;
        }
    }
    return old_Plant_GetRefreshTimeSeed(theSeedType, theImitaterType);
}

//--------------------------------------------------------------------------------------------------
//子弹分析,随机子弹实现
bool RandomBulletBUTT;
int BulletSpinnerChosenNum = -1;
bool IsOnlyPeaUseableButt;
bool BanCobCannonBullet, BanStar;
bool OnlyTouchFireWoodButt;

int (*old_ProjectileInitialize)(void *instance, int theX, int theY, int theRenderOrder, int theRow,
                                int theProjectileType);

int ProjectileInitialize(void *instance, int theX, int theY, int theRenderOrder, int theRow,
                         int theProjectileType) {

    if (instance != NULL && !OnlyTouchFireWoodButt) {
        //僵尸子弹与加农炮子弹NULL
        if (theProjectileType == 11 || theProjectileType == 13) {
            return old_ProjectileInitialize(instance, theX, theY, theRenderOrder, theRow,
                                            theProjectileType);
        }
        if (theProjectileType == 7 && BanStar) {
            return old_ProjectileInitialize(instance, theX, theY, theRenderOrder, theRow,
                                            theProjectileType);
        }
        if (IsOnlyPeaUseableButt && theProjectileType != 0) {
            return old_ProjectileInitialize(instance, theX, theY, theRenderOrder, theRow,
                                            theProjectileType);
        }
        if (BulletSpinnerChosenNum != -1) {
            theProjectileType = BulletSpinnerChosenNum;
        }
        if (RandomBulletBUTT) {
            theProjectileType = randomInt(1, 12);
        }
        if (theProjectileType == 11 && BanCobCannonBullet) {
            theProjectileType = randomInt(1, 10);//同时降低好友玉米黄油的概率!!
        }
    }
    if (instance != NULL&&theProjectileType==0) {
    // Use the random float to select the type with the given probabilities
    float rand_num = static_cast <float> (rand()) / static_cast <float> (RAND_MAX);
    if (rand_num < 0.30) {
        // 30% chance
        theProjectileType = 0;
    } else if (rand_num < 0.70) {
        // 40% chance
        theProjectileType = 1;
    } else {
        // 30% chance
        theProjectileType = 6;
        }
    }
    
    return old_ProjectileInitialize(instance, theX, theY, theRenderOrder, theRow,
                                    theProjectileType);
}

//---------------------------------------------------------------------------------------

//Board_AddPlant
bool PumpkinWithLadder;

int (*old_Board_AddPlant)(void *instance, int x, int y, int SeedType, int theImitaterType);

int Board_AddPlant(void *instance, int x, int y, int SeedType, int theImitaterType) {

    if (instance != NULL) {
        if (PumpkinWithLadder && SeedType == 30) {
            AddALadder(instance, x, y);
        }
        
    }
    return old_Board_AddPlant(instance, x, y, SeedType, theImitaterType);
}

//Plant_类
void *Plant_instanceBtn;

int (*old_Plant_Update)(void *instance);

int Plant_Update(void *instance) {
    Plant_instanceBtn = instance;
    if (instance != NULL) {
        if (ClearAllPlant) {
            Plant_Die(instance);
        }
    }
    return old_Plant_Update(instance);
}

//此函数将完全取代游戏原函数!!!!
bool NoSunLimit;

int AddSunMoney(int _this, int theAmount, int a3) {

    if (_this != NULL) {
        int v3; // r2
        int v4; // r1

        v3 = _this + 4 * a3 + 22016;
        v4 = theAmount + *(_DWORD *) (v3 + 116);
        if (NoSunLimit) {
            *(_DWORD *) (v3 + 116) = v4;
        } else {
            if (v4 > 9990)
                *(_DWORD *) (v3 + 116) = 9990;
            else
                *(_DWORD *) (v3 + 116) = v4;
        }
        return _this;

    }

}

bool FreePlantAt;

int (*old_CanPlantAt)(int _this, int a2, int a3, int a4);

int CanPlantAt(int _this, int a2, int a3, int a4) {

    if (_this != NULL) {
        if (FreePlantAt) {
            return 0; // 0<==>PLANTING_OK
        }
    }
    return old_CanPlantAt(_this, a2, a3, a4);
}

void (*old_ConvertToFireball)(int _this, int aGridX);

void ConvertToFireball(int _this, int aGridX) {

    if (_this != NULL && OnlyTouchFireWoodButt) {

        if (BulletSpinnerChosenNum != -1) {
            *((_DWORD *) _this + 27) = BulletSpinnerChosenNum;
            return;
        }
        if (RandomBulletBUTT) {
            *((_DWORD *) _this + 27) = randomInt(1, 12);
            return;
        }
    }
    return old_ConvertToFireball(_this, aGridX);
}

//此函数将完全取代游戏原函数!!!!
bool ColdPeaCanPassFireWood;

int ConvertToPea(int _this, int aGridX) {

    if (_this != NULL) {
        _DWORD *v2; // r4

        v2 = (_DWORD *) _this;
        if (*(_DWORD *) (_this + 136) != aGridX) {
            Attachment_AttachmentDie(_this + 140);
            v2[34] = aGridX;
            v2[27] = 0;
            //---****---
            if (ColdPeaCanPassFireWood) {
                v2[27] = 1;//冰豌豆
            }
            //---****---
            _this = LawnApp_PlayFoley(v2[4], 3);
        }
        return _this;
    }

}

bool ZombieCanNotWon = false;

void (*old_BoardZombiesWon)(int _this, int Zombie_this);

void BoardZombiesWon(int _this, int Zombie_this) {

    if (_this != NULL && Zombie_this != NULL) {
        if (ZombieCanNotWon) {
            ZOMBIE_ApplyBurn(Zombie_this);
            ZOMBIE_DieNoLoot(Zombie_this);
            return;
        }
    }
    return old_BoardZombiesWon(_this, Zombie_this);
}

bool enableConveyorBeltFast = false;

void UpdateConveyorBelt(int _this) {

    if (_this != NULL) {
        int v1; // r2
        int v2; // r2
        int v3; // r4
        int v4; // r3
        int v5; // r1
        bool v6; // cc
        int v7; // r1

        v1 = *(_DWORD *) (_this + 60) + 1;
        *(_DWORD *) (_this + 60) = v1;
        v2 = v1 & 3;
        if (!v2) {
            v3 = *(_DWORD *) (_this + 52);
            if (v3 > 0) {
                v4 = _this;
                do {
                    v5 = *(_DWORD *) (v4 + 136);
                    ++v2;
                    v4 += 116;
                    v6 = v5 <= 0;
                    v7 = 0;
                    if (!v6)
                        *(_DWORD *) (v4 + 20) = v7;
                } while (v2 != v3);
            }
        }
    }

}

int (*old_SeedBank_Draw)(int *instance, float a2);

int SeedBank_Draw(int *instance, float a2) {
    if (instance != NULL)
        maxPlantsNumInSeedBank = *((_DWORD *) instance + 13);
    return old_SeedBank_Draw(instance, a2);
}


int (*old_SeedChooserScreen_RebuildHelpbar)(int *instance);


int SeedChooserScreen_RebuildHelpbar(int *instance) {
    if (instance != NULL) {
        isImitaterEnabled =
                SeedChooserScreen_HasPacket(instance, 48, *((_BYTE *) instance + 3812)) != 0;
        int lawnApp = *((_DWORD *) instance + 931);
        if (lawnApp != NULL) {
            isStoreEnabled = LawnApp_CanShowStore(lawnApp);
            isAlmanacEnabled = LawnApp_CanShowAlmanac(lawnApp);
            isSurvivalRepick = CutScene_IsSurvivalRepick(
                    reinterpret_cast<void *>(*(_DWORD *) (*((_DWORD *) instance + 932) + 592)));
        }
    }
    return old_SeedChooserScreen_RebuildHelpbar(instance);
}


bool (*MainMenu_InTransition)(int *instance);

int (*MainMenu_SetScene)(int *instance, int scene);

int (*old_MainMenu_ButtonDepress)(int *instance, int a2);

int MainMenu_ButtonDepress(int *instance, int a2) {
    if (MainMenu_InTransition(instance))
        return old_MainMenu_ButtonDepress(instance, a2);
    if (*((_BYTE *) instance + 260))
        return old_MainMenu_ButtonDepress(instance, a2);
    if (instance[89] > 0)
        return old_MainMenu_ButtonDepress(instance, a2);
    if (a2 == 10 || a2 == 11) {
        int v6 = *instance;
        instance[87] = a2;
        (*(int (__fastcall **)(int *)) (v6 + 496))(instance);
        return old_MainMenu_ButtonDepress(instance, -1);
    }
    return old_MainMenu_ButtonDepress(instance, a2);
}


void Sexy_ScrollbarWidget_MouseDown(int a, int a1, int a2, int a3, int a4) {}


int totalGamesInThisPage = 0;
int gamesMin = 0;
int gamesMax = 0;

int (*old_ChallengeScreen_AddedToManager)(int *a, int a2);

int ChallengeScreen_AddedToManager(int *a, int a2) {
    isChallengeScreen = true;
    isMainMenu = false;
    isAwardScreen = false;
    isSeedChoosingNow = false;
    totalGamesInThisPage = a[376];
    gamesMin = a[378];
    gamesMax = gamesMin + totalGamesInThisPage;


    return old_ChallengeScreen_AddedToManager(a, a2);
}

int (*old_ChallengeScreen_Update)(int *a);

int ChallengeScreen_Update(int *a) {
    isChallengeScreen = true;
    if (requestSetScrollTarget) {

        if (totalGamesInThisPage > 0) {
            char *v7 = (char *) a + 748 + 4 * (a[186] + scrollTarget);
            int v8 = *((_DWORD *) v7 + 1);
            v8 = v8 >= gamesMax ? gamesMax : v8 <= gamesMin ? gamesMin : v8;

            a[378] = (v8 + scrollOffset) <= gamesMax ? v8 + scrollOffset : 88;
        }

        ChallengeScreen_SetScrollTarget(a, a[186] + scrollTarget);
//        a[377] = a[186] + 17;
//        a[378] = a[186] + 15;
        requestSetScrollTarget = false;

    }

//    if (requestNewGame) {
//        LawnApp_KillChallengeScreen(a[159]);
//        LawnApp_PreNewGame(a[159], a[377], a[377] != 76);
//        requestNewGame = false;
//    }
    return old_ChallengeScreen_Update(a);
}

int (*old_ChallengeScreen_RemovedFromManager)(int *a, int a2);

int ChallengeScreen_RemovedFromManager(int *a, int a2) {
    isChallengeScreen = false;
    return old_ChallengeScreen_RemovedFromManager(a, a2);
}

int (*old_LawnApp_ShowCreditScreen)(int a, bool a2);

int LawnApp_ShowCreditScreen(int a, bool a2) {
    isCreditScreen = true;
    if (a2) {
        LawnApp_KillMainMenu(a);
        LawnApp_KillNewOptionsDialog(a);
        (*(void (__fastcall **)(int, int)) (*(_DWORD *) a + 428))(a, 3);
    }
    return old_LawnApp_ShowCreditScreen(a, false);
}


int (*old_CreditScreen_RemovedFromManager)(int *a, int a2);

int CreditScreen_RemovedFromManager(int *a, int a2) {
    isCreditScreen = false;
    return old_CreditScreen_RemovedFromManager(a, a2);
}

int (*Coin_Collect)(int a1, int a2);

bool BanDropCoin;

int (*old_Coin_Update)(int a1);

int Coin_Update(int a1) {

    if (a1 != NULL) {
        if (BanDropCoin) {
            if (*((_DWORD *) a1 + 29) <= 6)
                *((_BYTE *) a1 + 80) = 1;
        }
        if (!enableManualCollect)
            if (*((_DWORD *) a1 + 29) == 30) {
                if (*((int *) a1 + 25) > 79 && !*((_BYTE *) a1 + 104)) {
                    Coin_Collect(a1, 0);
                }
            }
    }
    return old_Coin_Update(a1);
}

//int (*old_Plant_PlantInitialize)(int a1, int a2, int a3, int a4, int a5, int a6);
//
//int Plant_PlantInitialize(int a1, int a2, int a3, int a4, int a5, int a6) {
//    __android_log_print(ANDROID_LOG_DEBUG, "TAG", "%d %d",a4, a5);
//    return old_Plant_PlantInitialize(a1, a2, a3, a4, a5, a6);
//}

bool disableShop = false;

int LawnApp_CanShopLevel(int *a1) {
    return 0;
}


int (*old_Challenge_HeavyWeaponFire)(int a1, float a2, float a3);

int Challenge_HeavyWeaponFire(int a1, float a2, float a3) {

    if (a1 != NULL && a2 == 0 && a3 == 1) {
        a2 = angle1;
        a3 = angle2;
    }
    return old_Challenge_HeavyWeaponFire(a1, a2, a3);
}


bool StoreScreen_CanInteractWithButtons(int a) {
    bool result; // r0

    if (*((int *) a + 186) <= 119)
        return 0;
    result = *((_BYTE *) a + 756);
    if (result)
        return 0;
    if (*((int *) a + 195) <= 0)
        return *((_BYTE *) a + 809) ^ 1;
    return result;
}


int (*old_StoreScreen_Update)(int a);

int StoreScreen_Update(int a) {
    if (a != NULL) {
        isDaveStore = true;
        isMainMenu = false;
        isChallengeScreen = false;
        selectedStoreItem = *((_DWORD *) a + 194);
        canInteract = StoreScreen_CanInteractWithButtons(a);
        if (requestSetStoreSelectedSlot) {
            requestSetStoreSelectedSlot = false;
            StoreScreen_SetSelectedSlot(a, slotToSet);
        }
    }
    return old_StoreScreen_Update(a);
}


bool zenGardenItemNumIsZero(int *a1, int a2) {
    switch (a2) {
        case 7:
            return *(_DWORD *) (*(_DWORD *) (*((_DWORD *) a1 + 69) + 2352) + 500) == 1000;
        case 8:
            return *(_DWORD *) (*(_DWORD *) (*((_DWORD *) a1 + 69) + 2352) + 504) == 1000;
        case 10:
            return *(_DWORD *) (*(_DWORD *) (*((_DWORD *) a1 + 69) + 2352) + 548) == 1000;
        case 14:
            return *(_DWORD *) (*(_DWORD *) (*((_DWORD *) a1 + 69) + 2352) + 556) == 1000;
    }
    return false;
}

int (*old_Board_DrawZenButtons)(int *a1, int a2);

int Board_DrawZenButtons(int *a1, int a2) {

    if (a1 != NULL) {
        zenGardenObjectsCount = 0;
        isGoldWateringCan = *(_DWORD *) (*(_DWORD *) (*((_DWORD *) a1 + 69) + 2348) + 496);
        for (int i = 6; i != 19; ++i) {
            if (Board_CanUseGameObject(a1, i)) {
                zenGardenObjects[zenGardenObjectsCount] = zenGardenItemNumIsZero(a1, i) ? 0 : (i +
                                                                                               3);
                zenGardenObjectsCount++;
            }
        }
    }
    return old_Board_DrawZenButtons(a1, a2);
}

int (*old_GamepadControls_Draw)(int *instance, int *graphics);

int GamepadControls_Draw(int *instance, int *graphics) {
    if (instance != NULL) {
        if (requestDrawShovelInCursor) {
            int *board = (int *) *((int *) instance + 14);
            int cursorObject_1P = *((_DWORD *) board + 142);
            *(_DWORD *) (cursorObject_1P + 64) = 6;
            *(int *) (cursorObject_1P + 24) = boardPositionXToSet - 20;
            *(int *) (cursorObject_1P + 28) = boardPositionYToSet - 20;
        }
    }
    return old_GamepadControls_Draw(instance, graphics);
}

int (*WaitForSecondPlayerDialog_GameButtonDown)(int *a1, int a2, int a3);

int (*old_WaitForSecondPlayerDialog_KeyDown)(int *a1, int a2);

int WaitForSecondPlayerDialog_KeyDown(int *a1, int a2) {
//    __android_log_print(ANDROID_LOG_DEBUG, "TAG", "%d", a2);
//    if (a2 == 27) {
//        old_WaitForSecondPlayerDialog_KeyDown(a1, a2);
//        __android_log_print(ANDROID_LOG_DEBUG, "TAG", "%d", 54545);
//        return 0;
//    }

    WaitForSecondPlayerDialog_GameButtonDown(a1, 6, 0);
    return WaitForSecondPlayerDialog_GameButtonDown(a1, 6, 1);


}


int (*old_Plant_UpdateReanim)(int *a1);

int Plant_UpdateReanim(int *a1) {
    if (requestDrawShovelInCursor && a1 == (int *) plantUnderShovel && a1) {
        int newReam = LawnApp_ReanimationTryToGet(*((_DWORD *) plantUnderShovel + 4),
                                                  *((_DWORD *) plantUnderShovel + 41));
        if (newReam) {
            Reanimation_OverrideScale(newReam, 1.1f, 1.1f);
        }
        return newReam;
    }
    return old_Plant_UpdateReanim(a1);
}


int (*old_Zombie_Update)(int a1);

int Zombie_Update(int instance) {
    bool a = *(_BYTE *) (instance + 0xc8);
    if (a){
        *(_Float16*)(instance + 0x3c) = 0;
        //*(_Float16*)(instance + 0x3c) = 0;
    }
    return old_Zombie_Update(instance);
}


//bool requestVSSetup = false;
bool requestVSPlantSelectSeed = false;
bool requestVSZombieSelectSeed = false;
int plantX = 0;
int plantY = 0;
int zombieX = 0;
int zombieY = 0;

void (*VSSetupMenu_GameButtonDown)(int *a, int a2, unsigned int a3, int a4);

// a3 根据手柄决定是0还是1
// a4 恒定为0
// a2 是按键键值，对应关系如下：

// 左摇杆上 0
// 左摇杆下 1
// 左摇杆左 2
// 左摇杆右 3

// 未知键 4
// 暂停键 5

// A 6
// B 7
// X 8
// Y 9

// L1 10
// R1 11
// L2 12
// R2 13

// TL 14
// TR 15

// 上 16
// 下 17
// 左 18
// 右 19


int (*old_VSSetupMenu_Update)(int *a);


int VSSetupMenu_Update(int *a) {
    if (a) {
        isInVSSetupMenu = true;
        VSSetupState = *(_DWORD *) ((int) a + 296);



        int tmpSeedChooser1 = *(_DWORD *) (*((_DWORD *) a + 73) + 2244);
        if (VSSetupState == 3 && tmpSeedChooser1 == 0) {
            return (int) a;
        }
//        if (requestVSSetup) {
//            VSSetupMenu_GameButtonDown(a, 18, 0, 0);
//            VSSetupMenu_GameButtonDown(a, 19, 1, 0);
//            VSSetupMenu_GameButtonDown(a, 6, 0, 0);
//            VSSetupMenu_GameButtonDown(a, 6, 1, 0);
//            requestVSSetup = false;
//        }
        if (VSSetupState == 1) {
            VSSetupMenu_GameButtonDown(a, 18, 0, 0);
            VSSetupMenu_GameButtonDown(a, 19, 1, 0);
            VSSetupMenu_GameButtonDown(a, 6, 0, 0);
            VSSetupMenu_GameButtonDown(a, 6, 1, 0);
        }
        if (requestVSPlantSelectSeed) {
            for (int i = 0; i < 7; ++i) {
                VSSetupMenu_GameButtonDown(a, 18, 0, 0);
            }
            for (int i = 0; i < 4; ++i) {
                VSSetupMenu_GameButtonDown(a, 16, 0, 0);
            }
            for (int i = 0; i < plantX; ++i) {
                VSSetupMenu_GameButtonDown(a, 19, 0, 0);
            }
            for (int i = 0; i < plantY; ++i) {
                VSSetupMenu_GameButtonDown(a, 17, 0, 0);
            }
            VSSetupMenu_GameButtonDown(a, 6, 0, 0);
            requestVSPlantSelectSeed = false;
        }
        if (requestVSZombieSelectSeed) {
            for (int i = 0; i < 4; ++i) {
                VSSetupMenu_GameButtonDown(a, 18, 1, 0);
            }
            for (int i = 0; i < 3; ++i) {
                VSSetupMenu_GameButtonDown(a, 16, 1, 0);
            }
            for (int i = 0; i < zombieX; ++i) {
                VSSetupMenu_GameButtonDown(a, 19, 1, 0);
            }
            for (int i = 0; i < zombieY; ++i) {
                VSSetupMenu_GameButtonDown(a, 17, 1, 0);
            }
            VSSetupMenu_GameButtonDown(a, 6, 1, 0);
            requestVSZombieSelectSeed = false;
        }
    }
    return old_VSSetupMenu_Update(a);
}


bool isInVSResultsMenu = false;

int (*old_VSResultsMenu_Update)(int *a);

int VSResultsMenu_Update(int *a) {
    isInVSResultsMenu = true;
    return old_VSResultsMenu_Update(a);
}

int (*old_VSResultsMenu_OnExit)(int *a);

int VSResultsMenu_OnExit(int *a) {
    isInVSResultsMenu = false;
    return old_VSResultsMenu_OnExit(a);
}

void (*origZombie_ApllyChill)(int, int);
void Zombie_ApplyChill(int instance, bool isLongChill){
    int length = 100;
    if(isLongChill){
        length = 200;
    }
    *(_DWORD *) (instance + 0xc4) = length;
    int board = *(_DWORD *) (instance + 0x14);
    //if(BOARDINSTANCE==NULL) return;
    // Board_DisplayAdvice(BOARDINSTANCE, std::string("Display Advice test - Chilling "), 0, 0);
    
}

void CallHook() {

    //游戏函数
    Coin_Collect = (int (*)(int a1, int a2)) Coin_CollectAddr;
    GamepadControls_OnButtonDown = (int (*)(int a1, int a2, int a3,
                                            unsigned int a4)) GamepadControls_OnButtonDownAddr;
    VSSetupMenu_GameButtonDown = (void (*)(int *a1, int a2,
                                           unsigned int a3, int a4)) VSSetupMenu_GameButtonDownAddr;
    WaitForSecondPlayerDialog_GameButtonDown = (int (*)(int *a1, int a2,
                                                        int a3)) WaitForSecondPlayerDialog_GameButtonDownAddr;
    Board_GetTopPlantAt = (int (*)(int *a1, unsigned int a2, unsigned int a3,
                                   int a4)) Board_GetTopPlantAtAddr;
    Board_NewPlant = (int (*)(int *a1, int a2, int a3, int a4, int a5, int a6)) Board_NewPlantAddr;
    Board_CanUseGameObject = (int (*)(int *a1, int a2)) Board_CanUseGameObjectAddr;
    Board_ToolHitTest = (int (*)(int *a1, int a2, int a3)) Board_ToolHitTestAddr;
    StoreScreen_SetSelectedSlot = (int (*)(int a1, int a2)) StoreScreen_SetSelectedSlotAddr;
    LawnApp_IsWhackAZombieLevel = (int (*)(int *a)) LawnApp_IsWhackAZombieLevelAddr;
    LawnApp_IsScaryPotterLevel = (int (*)(int *a)) LawnApp_IsScaryPotterLevelAddr;
    SeedChooserScreen_HasPacket = (int (*)(int *a, int a2,
                                           bool a3)) SeedChooserScreen_HasPacketAddr;
    LawnApp_CanShowStore = (int (*)(int a)) LawnApp_CanShowStoreAddr;
    LawnApp_CanShowAlmanac = (int (*)(int a)) LawnApp_CanShowAlmanacAddr;
    LawnApp_KillNewOptionsDialog = (int (*)(int a)) LawnApp_KillNewOptionsDialogAddr;
    LawnApp_KillMainMenu = (int (*)(int a)) LawnApp_KillMainMenuAddr;
    LawnApp_KillChallengeScreen = (int (*)(int a)) LawnApp_KillChallengeScreenAddr;
    LawnApp_PreNewGame = (int (*)(int a, int a2, int a3)) LawnApp_PreNewGameAddr;
    ChallengeScreen_SetScrollTarget = (int (*)(int *result,
                                               int a)) ChallengeScreen_SetScrollTargetAddr;
    Board_GetPlantsOnLawn = (int (*)(int result, unsigned int a2, unsigned int a3,
                                     int *a4)) Board_GetPlantsOnLawnAddr;
    SeedChooserScreeen_Has7Rows = (bool (*)(void *instance)) SeedChooserScreeen_Has7RowsAddr;
    CutScene_IsSurvivalRepick = (bool (*)(void *instance)) CutScene_IsSurvivalRepickAddr;
    Board_GridToPixelX = (int (*)(void *result, unsigned int a2,
                                  unsigned int a3)) Board_GridToPixelXAddr;
    Board_GridToPixelY = (int (*)(void *result, unsigned int a2,
                                  unsigned int a3)) Board_GridToPixelYAddr;
    Board_PixelToGridY = (int (*)(void *result, unsigned int a2,
                                  unsigned int a3)) Board_PixelToGridYAddr;
    Board_PixelToGridX = (int (*)(void *result, unsigned int a2,
                                  unsigned int a3)) Board_PixelToGridXAddr;
    AddALadder = (unsigned int (*)(void *, int, int)) AddALadderAddr;
    AddPlant = (int (*)(void *, int, int, int, int)) AddPlantAddr;
    AddZombieInRow = (int (*)(void *, int, int, int)) AddZombieInRowAddr;
    Plant_Die = (int (*)(void *)) PlantDieAddr;
    LawnApp_PlayFoley = (int (*)(int, int)) LawnApp_PlayFoleyAddr;
    Attachment_AttachmentDie = (int (*)(int)) AttachmentDieAddr;
    ZOMBIE_DieNoLoot = (int (*)(int)) DieNoLootAddr;
    ZOMBIE_ApplyBurn = (int (*)(int)) ApplyBurnAddr;
    DoCheatDialog = (int (*)(int)) DoCheatDialogAddr;
    DoCheatCodeDialog = (int (*)(int)) DoCheatCodeDialogAddr;
    MainMenu_InTransition = (bool (*)(int *)) MainMenu_InTransitionAddr;
    MainMenu_SetScene = (int (*)(int *, int)) MainMenu_SetSceneAddr;
    Coin_Die = (int (*)(int)) Coin_DieAddr;
    GamepadControls_OnKeyDown = (int (*)(int a1, int a2,
                                         unsigned int a3)) GamepadControls_OnKeyDownAddr;
    GamepadControls_GetSeedBank = (int (*)(int)) GamepadControls_GetSeedBankAddr;
    Board_ClearCursor = (int (*)(int *, int)) Board_ClearCursorAddr;
    Board_UpdateGame = (int (*)(void *)) Board_UpdateGameAddr;
    LawnApp_ReanimationTryToGet = (int (*)(int, int)) LawnApp_ReanimationTryToGetAddr;
    Board_DisplayAdvice = (void (*)(int*, std::string const&, uint, uint16_t)) Board_DisplayAdviceAddr;


    MSHookFunction((void *) Board_UpdateAddr, (void *) Board_Update, (void **) &old_Board_Update);
    MSHookFunction((void *) CutSceneUpdateAddr, (void *) CutScene_Update,
                   (void **) &old_CutScene_Update);
    MSHookFunction((void *) MainMenuUpdateAddr, (void *) MainMenu_Update,
                   (void **) &old_MainMenu_Update);
    MSHookFunction((void *) SeedChooserScreenEnableStartButtonAddr,
                   (void *) SeedChooserScreen_EnableStartButton,
                   (void **) &old_SeedChooserScreen_EnableStartButton);
    MSHookFunction((void *) LawnAppShowAwardScreenAwardTypeAddr, (void *) LawnApp_ShowAwardScreen,
                   (void **) &old_LawnApp_ShowAwardScreen);
    MSHookFunction((void *) LawnAppKillAwardScreenAddr, (void *) LawnApp_KillAwardScreen,
                   (void **) &old_LawnApp_KillAwardScreen);
    MSHookFunction((void *) SexyDialogAddedToManagerWidgetManagerAddr,
                   (void *) SexyDialog_AddedToManager, (void **) &old_SexyDialog_AddedToManager);
    MSHookFunction((void *) SexyDialogRemovedFromManagerAddr,
                   (void *) SexyDialog_RemovedFromManager,
                   (void **) &old_SexyDialog_RemovedFromManager);
    MSHookFunction((void *) ImitaterDialogAddr, (void *) ImitaterDialog_ImitaterDialog,
                   (void **) &old_ImitaterDialog_ImitaterDialog);
    MSHookFunction((void *) AlmanacDialog_AddedToManagerAddr, (void *) AlmanacDialog_AddedToManager,
                   (void **) &old_AlmanacDialog_AddedToManager);
    MSHookFunction((void *) AlmanacDialog_RemovedFromManagerAddr,
                   (void *) AlmanacDialog_RemovedFromManager,
                   (void **) &old_AlmanacDialog_RemovedFromManager);

    MSHookFunction((void *) StoreScreen_AddedToManagerAddr, (void *) StoreScreen_AddedToManager,
                   (void **) &old_StoreScreen_AddedToManager);
    MSHookFunction((void *) StoreScreen_RemovedFromManagerAddr,
                   (void *) StoreScreen_RemovedFromManager,
                   (void **) &old_StoreScreen_RemovedFromManager);
    MSHookFunction((void *) LawnAppUpdateAppAddr, (void *) LawnApp_UpDateApp,
                   (void **) &old_LawnApp_UpDateApp);
    MSHookFunction((void *) GamepadControlsButtonDownFireCobcannonTestAddr,
                   (void *) GamepadControls_ButtonDownFireCobcannonTest,
                   (void **) &old_GamepadControls_ButtonDownFireCobcannonTest);
    MSHookFunction((void *) SeedBank_DrawAddr,
                   (void *) SeedBank_Draw,
                   (void **) &old_SeedBank_Draw);
    MSHookFunction((void *) SeedChooserScreen_RebuildHelpbarAddr,
                   (void *) SeedChooserScreen_RebuildHelpbar,
                   (void **) &old_SeedChooserScreen_RebuildHelpbar);
    MSHookFunction((void *) MainMenu_ButtonDepressAddr,
                   (void *) MainMenu_ButtonDepress,
                   (void **) &old_MainMenu_ButtonDepress);

    //菜单Hook
    MSHookFunction((void *) TakeSunMoneyAddr, (void *) Board_TakeSunMoney,
                   (void **) &old_Board_TakeSunMoney);
    MSHookFunction((void *) SeedPacketUpdateAddr, (void *) SeedPacket_Update,
                   (void **) &old_SeedPacket_Update);
    MSHookFunction((void *) GetRefreshTimeSeedAddr, (void *) Plant_GetRefreshTimeSeed,
                   (void **) &old_Plant_GetRefreshTimeSeed);
    MSHookFunction((void *) AddPlantAddr, (void *) Board_AddPlant, (void **) &old_Board_AddPlant);
    MSHookFunction((void *) ProjectileInitializeAddr, (void *) ProjectileInitialize,
                   (void **) &old_ProjectileInitialize);
    MSHookFunction((void *) PlantUpdateAddr, (void *) Plant_Update, (void **) &old_Plant_Update);
    MSHookFunction((void *) AddSunMoneyAddr, (void *) AddSunMoney, NULL);
    MSHookFunction((void *) CanPlantAtAddr, (void *) CanPlantAt, (void **) &old_CanPlantAt);
    MSHookFunction((void *) ConvertToFireballAddr, (void *) ConvertToFireball,
                   (void **) &old_ConvertToFireball);
    MSHookFunction((void *) ConvertToPeaAddr, (void *) ConvertToPea, NULL);
    MSHookFunction((void *) BoardZombiesWonAddr, (void *) BoardZombiesWon,
                   (void **) &old_BoardZombiesWon);
    MSHookFunction((void *) Sexy_ScrollbarWidget_MouseDownAddr,
                   (void *) Sexy_ScrollbarWidget_MouseDown, NULL);
    MSHookFunction((void *) ChallengeScreen_AddedToManagerAddr,
                   (void *) ChallengeScreen_AddedToManager,
                   (void **) &old_ChallengeScreen_AddedToManager);
    MSHookFunction((void *) ChallengeScreen_RemovedFromManagerAddr,
                   (void *) ChallengeScreen_RemovedFromManager,
                   (void **) &old_ChallengeScreen_RemovedFromManager);
    MSHookFunction((void *) ChallengeScreen_UpdateAddr, (void *) ChallengeScreen_Update,
                   (void **) &old_ChallengeScreen_Update);
    MSHookFunction((void *) LawnApp_ShowCreditScreenAddr, (void *) LawnApp_ShowCreditScreen,
                   (void **) &old_LawnApp_ShowCreditScreen);
    MSHookFunction((void *) CreditScreen_RemovedFromManagerAddr,
                   (void *) CreditScreen_RemovedFromManager,
                   (void **) &old_CreditScreen_RemovedFromManager);
    MSHookFunction((void *) Coin_UpadteAddr, (void *) Coin_Update,
                   (void **) &old_Coin_Update);
    MSHookFunction((void *) StoreScreen_UpdateAddr, (void *) StoreScreen_Update,
                   (void **) &old_StoreScreen_Update);
    MSHookFunction((void *) Challenge_HeavyWeaponFireAddr, (void *) Challenge_HeavyWeaponFire,
                   (void **) &old_Challenge_HeavyWeaponFire);
    MSHookFunction((void *) Board_DrawZenButtonsAddr, (void *) Board_DrawZenButtons,
                   (void **) &old_Board_DrawZenButtons);
    MSHookFunction((void *) GamepadControls_DrawAddr, (void *) GamepadControls_Draw,
                   (void **) &old_GamepadControls_Draw);
    MSHookFunction((void *) Plant_UpdateReanimAddr,
                   (void *) Plant_UpdateReanim,
                   (void **) &old_Plant_UpdateReanim);
    MSHookFunction((void *) WaitForSecondPlayerDialog_KeyDownAddr,
                   (void *) WaitForSecondPlayerDialog_KeyDown,
                   (void **) old_WaitForSecondPlayerDialog_KeyDown);
    MSHookFunction((void *) Zombie_UpdateAddr,
                    (void *) Zombie_Update,
                    (void **) &old_Zombie_Update);
    MSHookFunction((void *) VSSetupMenu_UpdateAddr,
                   (void *) VSSetupMenu_Update,
                   (void **) &old_VSSetupMenu_Update);
    MSHookFunction((void *) VSResultsMenu_UpdateAddr,
                   (void *) VSResultsMenu_Update,
                   (void **) &old_VSResultsMenu_Update);
    MSHookFunction((void *) VSResultsMenu_OnExitAddr,
                   (void *) VSResultsMenu_OnExit,
                   (void **) &old_VSResultsMenu_OnExit);
    if (disableShop) {
        MSHookFunction((void *) LawnApp_CanShopLevelAddr, (void *) LawnApp_CanShopLevel, NULL);
    }
    if (enableConveyorBeltFast) {
        MSHookFunction((void *) UpdateConveyorBeltAddr, (void *) UpdateConveyorBelt, NULL);
    }
    // magic pvz hook
    MSHookFunction(
        (void*) Zombie_ApplyChillAddr,
        (void*) Zombie_ApplyChill,
        (void**) origZombie_ApllyChill
    );

}

void GetedChangeFormationID(int Madoka) {

    switch (Madoka) {
        case -1:
            ChangeFormationArr[0][0] = 1314;
            IsRoofButt = 0;
            FormationButt = 0;

            break;
        case 0:
            memcpy(ChangeFormationArr, PE电波钟无炮, sizeof(PE电波钟无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 1:
            memcpy(ChangeFormationArr, PE最简无炮, sizeof(PE最简无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 2:
            memcpy(ChangeFormationArr, PE伪无伤无炮, sizeof(PE伪无伤无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 3:
            memcpy(ChangeFormationArr, PE自然控丑无炮, sizeof(PE自然控丑无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;


        case 4:
            memcpy(ChangeFormationArr, PE火焰无炮, sizeof(PE火焰无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 5:
            memcpy(ChangeFormationArr, PE分裂火焰无炮, sizeof(PE分裂火焰无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 6:
            memcpy(ChangeFormationArr, PE后退无炮, sizeof(PE后退无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 7:
            memcpy(ChangeFormationArr, PE超前置无炮, sizeof(PE超前置无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 8:
            memcpy(ChangeFormationArr, PE王子无炮, sizeof(PE王子无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 9:
            memcpy(ChangeFormationArr, PE机械钟无炮, sizeof(PE机械钟无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 10:
            memcpy(ChangeFormationArr, PE神之无炮, sizeof(PE神之无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 11:
            memcpy(ChangeFormationArr, PE石英钟无炮, sizeof(PE石英钟无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;


        case 12:
            memcpy(ChangeFormationArr, PE靠天无炮, sizeof(PE靠天无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 13:
            memcpy(ChangeFormationArr, PE方块无神无炮, sizeof(PE方块无神无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 14:
            memcpy(ChangeFormationArr, PE56加速无神无炮, sizeof(PE56加速无神无炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 15:
            memcpy(ChangeFormationArr, PE压制一炮, sizeof(PE压制一炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 16:
            memcpy(ChangeFormationArr, PE小二炮, sizeof(PE小二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 17:
            memcpy(ChangeFormationArr, PE火焰二炮, sizeof(PE火焰二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 18:
            memcpy(ChangeFormationArr, PE核武二炮, sizeof(PE核武二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 19:
            memcpy(ChangeFormationArr, PE分裂二炮, sizeof(PE分裂二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 20:
            memcpy(ChangeFormationArr, PE方正二炮, sizeof(PE方正二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;


        case 21:
            memcpy(ChangeFormationArr, PE经典二炮, sizeof(PE经典二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 22:
            memcpy(ChangeFormationArr, PE冲关三炮, sizeof(PE冲关三炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 23:
            memcpy(ChangeFormationArr, PE太极四炮, sizeof(PE太极四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 24:
            memcpy(ChangeFormationArr, PE全金属四炮, sizeof(PE全金属四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 25:
            memcpy(ChangeFormationArr, PE方块四炮, sizeof(PE方块四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 26:
            memcpy(ChangeFormationArr, PE青四炮, sizeof(PE青四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 27:
            memcpy(ChangeFormationArr, PE水路无植物四炮, sizeof(PE水路无植物四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 28:
            memcpy(ChangeFormationArr, PE方四炮, sizeof(PE方四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;


        case 29:
            memcpy(ChangeFormationArr, PE神之四炮, sizeof(PE神之四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 30:
            memcpy(ChangeFormationArr, PE双核底线四炮, sizeof(PE双核底线四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 31:
            memcpy(ChangeFormationArr, PE经典四炮, sizeof(PE经典四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 32:
            memcpy(ChangeFormationArr, PE火焰四炮, sizeof(PE火焰四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 33:
            memcpy(ChangeFormationArr, PE底线四炮, sizeof(PE底线四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 34:
            memcpy(ChangeFormationArr, PE传统四炮, sizeof(PE传统四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 35:
            memcpy(ChangeFormationArr, PE半场无植物五炮, sizeof(PE半场无植物五炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 36:
            memcpy(ChangeFormationArr, PE散炸五炮, sizeof(PE散炸五炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 37:
            memcpy(ChangeFormationArr, PE心五炮, sizeof(PE心五炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 38:
            memcpy(ChangeFormationArr, PE陆路无植物六炮, sizeof(PE陆路无植物六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 39:
            memcpy(ChangeFormationArr, PE水路无植物六炮, sizeof(PE水路无植物六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 40:
            memcpy(ChangeFormationArr, PE青苔六炮, sizeof(PE青苔六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 41:
            memcpy(ChangeFormationArr, PE禅房花木深, sizeof(PE禅房花木深));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 42:
            memcpy(ChangeFormationArr, PE神之六炮, sizeof(PE神之六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 43:
            memcpy(ChangeFormationArr, PE玉米六炮, sizeof(PE玉米六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 44:
            memcpy(ChangeFormationArr, PE空炸六炮, sizeof(PE空炸六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 45:
            memcpy(ChangeFormationArr, PE超后置六炮, sizeof(PE超后置六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 46:
            memcpy(ChangeFormationArr, PE方六炮, sizeof(PE方六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 47:
            memcpy(ChangeFormationArr, PE蝶韵, sizeof(PE蝶韵));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 48:
            memcpy(ChangeFormationArr, PE一勺汤圆, sizeof(PE一勺汤圆));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 49:
            memcpy(ChangeFormationArr, PE间隔无植物七炮, sizeof(PE间隔无植物七炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 50:
            memcpy(ChangeFormationArr, PE玉兔茕茕, sizeof(PE玉兔茕茕));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 51:
            memcpy(ChangeFormationArr, PE无保护八炮, sizeof(PE无保护八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 52:
            memcpy(ChangeFormationArr, PE树八炮, sizeof(PE树八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 53:
            memcpy(ChangeFormationArr, PE全对称树八炮, sizeof(PE全对称树八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 54:
            memcpy(ChangeFormationArr, PE矩形八炮, sizeof(PE矩形八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 55:
            memcpy(ChangeFormationArr, PE神之八炮, sizeof(PE神之八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;

        case 56:
            memcpy(ChangeFormationArr, PE阴阳八炮, sizeof(PE阴阳八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 57:
            memcpy(ChangeFormationArr, PE浮萍八炮, sizeof(PE浮萍八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 58:
            memcpy(ChangeFormationArr, PE后置八炮, sizeof(PE后置八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 59:
            memcpy(ChangeFormationArr, PE饲养海豚, sizeof(PE饲养海豚));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 60:
            memcpy(ChangeFormationArr, PE玉米八炮, sizeof(PE玉米八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 61:
            memcpy(ChangeFormationArr, PE经典八炮, sizeof(PE经典八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 62:
            memcpy(ChangeFormationArr, PE花海八炮, sizeof(PE花海八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 63:
            memcpy(ChangeFormationArr, PEC2八炮, sizeof(PEC2八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 64:
            memcpy(ChangeFormationArr, PE分离八炮, sizeof(PE分离八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 65:
            memcpy(ChangeFormationArr, PE全对称八炮, sizeof(PE全对称八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 66:
            memcpy(ChangeFormationArr, PE3C八炮, sizeof(PE3C八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 67:
            memcpy(ChangeFormationArr, PE灯台八炮, sizeof(PE灯台八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 68:
            memcpy(ChangeFormationArr, PE9炮, sizeof(PE9炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 69:
            memcpy(ChangeFormationArr, PE方块九炮, sizeof(PE方块九炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 70:
            memcpy(ChangeFormationArr, PEC6i九炮, sizeof(PEC6i九炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 71:
            memcpy(ChangeFormationArr, PE心九炮, sizeof(PE心九炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 72:
            memcpy(ChangeFormationArr, PE轮炸九炮, sizeof(PE轮炸九炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 73:
            memcpy(ChangeFormationArr, PE2炮, sizeof(PE2炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 74:
            memcpy(ChangeFormationArr, PE六芒星十炮, sizeof(PE六芒星十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 75:
            memcpy(ChangeFormationArr, PE六边形十炮, sizeof(PE六边形十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 76:
            memcpy(ChangeFormationArr, PE方块十炮, sizeof(PE方块十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 77:
            memcpy(ChangeFormationArr, PE斜方十炮, sizeof(PE斜方十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 78:
            memcpy(ChangeFormationArr, PE简化十炮, sizeof(PE简化十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 79:
            memcpy(ChangeFormationArr, PE后置十炮, sizeof(PE后置十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 80:
            memcpy(ChangeFormationArr, PE经典十炮, sizeof(PE经典十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 81:
            memcpy(ChangeFormationArr, PE六线囚尸, sizeof(PE六线囚尸));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 82:
            memcpy(ChangeFormationArr, PE斜十炮, sizeof(PE斜十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 83:
            memcpy(ChangeFormationArr, PE魔方十炮, sizeof(PE魔方十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 84:
            memcpy(ChangeFormationArr, PE戴夫的小汉堡, sizeof(PE戴夫的小汉堡));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 85:
            memcpy(ChangeFormationArr, PE鸡尾酒, sizeof(PE鸡尾酒));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 86:
            memcpy(ChangeFormationArr, PE一勺汤圆十二炮, sizeof(PE一勺汤圆十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 87:
            memcpy(ChangeFormationArr, PE玉壶春十二炮, sizeof(PE玉壶春十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 88:
            memcpy(ChangeFormationArr, PE半场十二炮, sizeof(PE半场十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 89:
            memcpy(ChangeFormationArr, PE简化十二炮, sizeof(PE简化十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 90:
            memcpy(ChangeFormationArr, PE经典十二炮, sizeof(PE经典十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 91:
            memcpy(ChangeFormationArr, PE火焰十二炮, sizeof(PE火焰十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 92:
            memcpy(ChangeFormationArr, PE冰雨十二炮·改, sizeof(PE冰雨十二炮·改));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 93:
            memcpy(ChangeFormationArr, PE冰雨十二炮·改改, sizeof(PE冰雨十二炮·改改));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 94:
            memcpy(ChangeFormationArr, PE单紫卡十二炮, sizeof(PE单紫卡十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 95:
            memcpy(ChangeFormationArr, PE神柱十二炮, sizeof(PE神柱十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 96:
            memcpy(ChangeFormationArr, PE神之十二炮, sizeof(PE神之十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 97:
            memcpy(ChangeFormationArr, PE水路无植物十二炮, sizeof(PE水路无植物十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 98:
            memcpy(ChangeFormationArr, PE纯白悬空十二炮, sizeof(PE纯白悬空十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 99:
            memcpy(ChangeFormationArr, PE后花园十二炮, sizeof(PE后花园十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 100:
            memcpy(ChangeFormationArr, PE玉米十二炮, sizeof(PE玉米十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 101:
            memcpy(ChangeFormationArr, PE两路狂暴, sizeof(PE两路狂暴));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 102:
            memcpy(ChangeFormationArr, PE九列十二炮, sizeof(PE九列十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 103:
            memcpy(ChangeFormationArr, PE梯曾十二炮, sizeof(PE梯曾十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 104:
            memcpy(ChangeFormationArr, PE君海十二炮, sizeof(PE君海十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 105:
            memcpy(ChangeFormationArr, PE箜篌引, sizeof(PE箜篌引));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 106:
            memcpy(ChangeFormationArr, PE梅花十三, sizeof(PE梅花十三));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 107:
            memcpy(ChangeFormationArr, PE最后之作, sizeof(PE最后之作));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 108:
            memcpy(ChangeFormationArr, PE冰心灯, sizeof(PE冰心灯));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 109:
            memcpy(ChangeFormationArr, PE太极十四炮, sizeof(PE太极十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 110:
            memcpy(ChangeFormationArr, PE真·四炮, sizeof(PE真·四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 111:
            memcpy(ChangeFormationArr, PE神棍十四炮, sizeof(PE神棍十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 112:
            memcpy(ChangeFormationArr, PE神之十四炮, sizeof(PE神之十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 113:
            memcpy(ChangeFormationArr, PE穿越十四炮, sizeof(PE穿越十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 114:
            memcpy(ChangeFormationArr, PE钻石十五炮, sizeof(PE钻石十五炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 115:
            memcpy(ChangeFormationArr, PE神之十五炮, sizeof(PE神之十五炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 116:
            memcpy(ChangeFormationArr, PE真·二炮, sizeof(PE真·二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 117:
            memcpy(ChangeFormationArr, PE冰箱灯, sizeof(PE冰箱灯));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 118:
            memcpy(ChangeFormationArr, PE炮环十二花, sizeof(PE炮环十二花));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 119:
            memcpy(ChangeFormationArr, PE单冰十六炮, sizeof(PE单冰十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 120:
            memcpy(ChangeFormationArr, PE对称十六炮, sizeof(PE对称十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 121:
            memcpy(ChangeFormationArr, PE神之十六炮, sizeof(PE神之十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 122:
            memcpy(ChangeFormationArr, PE裸奔十六炮, sizeof(PE裸奔十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 123:
            memcpy(ChangeFormationArr, PE双冰十六炮, sizeof(PE双冰十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 124:
            memcpy(ChangeFormationArr, PE超前置十六炮, sizeof(PE超前置十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 125:
            memcpy(ChangeFormationArr, PE火焰十六炮, sizeof(PE火焰十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 126:
            memcpy(ChangeFormationArr, PE经典十六炮, sizeof(PE经典十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 127:
            memcpy(ChangeFormationArr, PE折线十六炮, sizeof(PE折线十六炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 128:
            memcpy(ChangeFormationArr, PE肺十八炮_极限, sizeof(PE肺十八炮_极限));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 129:
            memcpy(ChangeFormationArr, PE纯十八炮, sizeof(PE纯十八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 130:
            memcpy(ChangeFormationArr, PE真·十八炮, sizeof(PE真·十八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 131:
            memcpy(ChangeFormationArr, PE冰魄十八炮, sizeof(PE冰魄十八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 132:
            memcpy(ChangeFormationArr, PE尾炸十八炮, sizeof(PE尾炸十八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 133:
            memcpy(ChangeFormationArr, PE经典十八炮, sizeof(PE经典十八炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 134:
            memcpy(ChangeFormationArr, PE纯二十炮, sizeof(PE空炸二十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 135:
            memcpy(ChangeFormationArr, PE空炸二十炮, sizeof(PE空炸二十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 136:
            memcpy(ChangeFormationArr, PE钉耙二十炮, sizeof(PE钉耙二十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 137:
            memcpy(ChangeFormationArr, PE新二十炮, sizeof(PE新二十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 138:
            memcpy(ChangeFormationArr, PE无冰瓜二十炮, sizeof(PE无冰瓜二十炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 139:
            memcpy(ChangeFormationArr, PE绝望之路, sizeof(PE绝望之路));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 140:
            memcpy(ChangeFormationArr, PE二十一炮, sizeof(PE二十一炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 141:
            memcpy(ChangeFormationArr, PE新二十二炮, sizeof(PE新二十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 142:
            memcpy(ChangeFormationArr, PE二十二炮, sizeof(PE二十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 143:
            memcpy(ChangeFormationArr, PE无冰瓜二十二炮, sizeof(PE无冰瓜二十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 144:
            memcpy(ChangeFormationArr, PE九列二十二炮, sizeof(PE九列二十二炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 145:
            memcpy(ChangeFormationArr, PE二十四炮, sizeof(PE二十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 146:
            memcpy(ChangeFormationArr, PE垫材二十四炮, sizeof(PE垫材二十四炮));
            IsRoofButt = 0;
            FormationButt = 1;
            break;
        case 147:
            memcpy(ChangeFormationArr, PE胆守_极限, sizeof(PE胆守_极限));
            IsRoofButt = 0;
            FormationButt = 1;
            break;


    }


}

#endif //PVZ_TV_1_1_5_HOOKGAME_H
