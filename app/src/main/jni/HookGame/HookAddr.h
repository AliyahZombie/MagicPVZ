//
// Created by Homura on 2023/5/11.
//

#ifndef PVZ_TV_1_1_5_HOOKADDR_H
#define PVZ_TV_1_1_5_HOOKADDR_H

#define targetLibName "libGameMain.so"

uintptr_t Board_UpdateAddr;
uintptr_t CutSceneUpdateAddr;
uintptr_t MainMenuUpdateAddr;
uintptr_t SeedChooserScreenEnableStartButtonAddr;
uintptr_t LawnAppShowAwardScreenAwardTypeAddr;
uintptr_t LawnAppKillAwardScreenAddr;
uintptr_t SexyDialogAddedToManagerWidgetManagerAddr;
uintptr_t SexyDialogRemovedFromManagerAddr;
uintptr_t ImitaterDialogAddr;
uintptr_t AlmanacDialog_AddedToManagerAddr;
uintptr_t AlmanacDialog_RemovedFromManagerAddr;
uintptr_t StoreScreen_AddedToManagerAddr;
uintptr_t StoreScreen_RemovedFromManagerAddr;
uintptr_t LawnAppUpdateAppAddr;
uintptr_t Board_GetPlantsOnLawnAddr;
uintptr_t GamepadControlsButtonDownFireCobcannonTestAddr;
uintptr_t Board_GridToPixelXAddr;
uintptr_t Board_GridToPixelYAddr;
uintptr_t Board_PixelToGridXAddr;
uintptr_t Board_PixelToGridYAddr;
uintptr_t SeedChooserScreeen_Has7RowsAddr;
uintptr_t SeedBank_DrawAddr;
uintptr_t CutScene_IsSurvivalRepickAddr;
uintptr_t SeedChooserScreen_RebuildHelpbarAddr;
uintptr_t MainMenu_ButtonDepressAddr;
uintptr_t MainMenu_InTransitionAddr;
uintptr_t MainMenu_SetSceneAddr;
uintptr_t Sexy_ScrollbarWidget_MouseDownAddr;
uintptr_t ChallengeScreen_AddedToManagerAddr;
uintptr_t ChallengeScreen_RemovedFromManagerAddr;
uintptr_t ChallengeScreen_SetScrollTargetAddr;
uintptr_t ChallengeScreen_UpdateAddr;
uintptr_t LawnApp_KillChallengeScreenAddr;
uintptr_t LawnApp_PreNewGameAddr;
uintptr_t LawnApp_ShowCreditScreenAddr;
uintptr_t LawnApp_KillMainMenuAddr;
uintptr_t LawnApp_KillNewOptionsDialogAddr;
//uintptr_t Plant_PlantInitializeAddr;
uintptr_t LawnApp_CanShowStoreAddr;
uintptr_t LawnApp_CanShowAlmanacAddr;
uintptr_t SeedChooserScreen_HasPacketAddr;
uintptr_t LawnApp_IsScaryPotterLevelAddr;
uintptr_t LawnApp_IsWhackAZombieLevelAddr;
uintptr_t LawnApp_CanShopLevelAddr;
uintptr_t Coin_DieAddr;
uintptr_t Coin_UpadteAddr;
uintptr_t StoreScreen_SetSelectedSlotAddr;
uintptr_t StoreScreen_UpdateAddr;
uintptr_t Challenge_HeavyWeaponFireAddr;
//uintptr_t LawnApp_IsChallengeWithoutSeedBankAddr;
//uintptr_t GridItem_DrawScaryPotAddr;
uintptr_t Board_MouseHitTestAddr;
uintptr_t Board_ToolHitTestAddr;
uintptr_t Board_CanUseGameObjectAddr;
uintptr_t Board_DrawZenButtonsAddr;
uintptr_t ZenGarden_RebuildHelpbarAddr;
uintptr_t CreditScreen_RemovedFromManagerAddr;
uintptr_t GamepadControls_OnKeyDownAddr;
uintptr_t GamepadControls_GetSeedBankAddr;
uintptr_t Board_ClearCursorAddr;
uintptr_t Board_UpdateGameAddr;
uintptr_t GamepadControls_DrawAddr;
uintptr_t CursorObject_DrawAddr;
uintptr_t WaitForSecondPlayerDialog_GameButtonDownAddr;
uintptr_t Board_MouseDownWithToolAddr;
uintptr_t LawnApp_ReanimationTryToGetAddr;
uintptr_t Plant_UpdateReanimAddr;
uintptr_t Board_CountPlantByTypeAddr;
uintptr_t Board_SetTutorialStateAddr;
uintptr_t Board_NewPlantAddr;
uintptr_t Board_GetTopPlantAtAddr;
uintptr_t Sexy_GamepadApp_CheckGamepadAddr;
uintptr_t Sexy_GamepadApp_HasGamepadAddr;
uintptr_t Zombie_UpdateAddr;
uintptr_t WaitForSecondPlayerDialog_KeyDownAddr;
uintptr_t SeedChooserScreen_SeedChooserScreenAddr;
uintptr_t VSSetupMenu_GameButtonDownAddr;
uintptr_t VSSetupMenu_UpdateAddr;
uintptr_t GamepadControls_OnButtonDownAddr;
uintptr_t Coin_CollectAddr;
uintptr_t VSResultsMenu_OnExitAddr;
uintptr_t VSResultsMenu_UpdateAddr;
//菜单涉及函数
uintptr_t TakeSunMoneyAddr;
uintptr_t SeedPacketUpdateAddr;
uintptr_t GetRefreshTimeSeedAddr;
uintptr_t AddALadderAddr;
uintptr_t AddPlantAddr;
uintptr_t AddZombieInRowAddr;
uintptr_t PlantDieAddr;
uintptr_t ProjectileInitializeAddr;
uintptr_t PlantUpdateAddr;
uintptr_t AddSunMoneyAddr;
uintptr_t MouseDownWithPlantAddr;
uintptr_t CanPlantAtAddr;
uintptr_t ConvertToFireballAddr;
uintptr_t ConvertToPeaAddr;
uintptr_t AttachmentDieAddr;
uintptr_t LawnApp_PlayFoleyAddr;
uintptr_t BoardZombiesWonAddr;
uintptr_t DieNoLootAddr;
uintptr_t ApplyBurnAddr;
uintptr_t UpdateConveyorBeltAddr;
uintptr_t DoCheatDialogAddr;
uintptr_t DoCheatCodeDialogAddr;

void Call_GetFunctionAddr() {

    void *handle = dlopen(targetLibName, 4);

    //触控涉及的函数地址
    Board_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board6UpdateEv"));
    CutSceneUpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8CutScene6UpdateEv"));
    MainMenuUpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8MainMenu6UpdateEv"));
    SeedChooserScreenEnableStartButtonAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN17SeedChooserScreen17EnableStartButtonEb"));
    LawnAppShowAwardScreenAwardTypeAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN7LawnApp15ShowAwardScreenE9AwardType"));
    LawnAppKillAwardScreenAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp15KillAwardScreenEv"));
    SexyDialogAddedToManagerWidgetManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN4Sexy6Dialog14AddedToManagerEPNS_13WidgetManagerE"));
    SexyDialogRemovedFromManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN4Sexy6Dialog18RemovedFromManagerEPNS_13WidgetManagerE"));
    ImitaterDialogAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN14ImitaterDialogC2Ei"));
    LawnAppUpdateAppAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp9UpdateAppEv"));
    GamepadControlsButtonDownFireCobcannonTestAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN15GamepadControls27ButtonDownFireCobcannonTestEv"));
    AlmanacDialog_AddedToManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN13AlmanacDialog14AddedToManagerEPN4Sexy13WidgetManagerE"));
    AlmanacDialog_RemovedFromManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN13AlmanacDialog18RemovedFromManagerEPN4Sexy13WidgetManagerE"));
    StoreScreen_AddedToManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN11StoreScreen14AddedToManagerEPN4Sexy13WidgetManagerE"));
    StoreScreen_RemovedFromManagerAddr = reinterpret_cast<uintptr_t>( dlsym(handle,"_ZN11StoreScreen18RemovedFromManagerEPN4Sexy13WidgetManagerE"));
    SeedBank_DrawAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN8SeedBank4DrawEPN4Sexy8GraphicsE"));
    CutScene_IsSurvivalRepickAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8CutScene16IsSurvivalRepickEv"));
    SeedChooserScreen_RebuildHelpbarAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN17SeedChooserScreen14RebuildHelpbarEv"));
    //要调用的游戏函数地址
    Board_GetPlantsOnLawnAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN5Board15GetPlantsOnLawnEiiP12PlantsOnLawn"));
    Board_GridToPixelXAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12GridToPixelXEii"));
    Board_GridToPixelYAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12GridToPixelYEii"));
    SeedChooserScreeen_Has7RowsAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN17SeedChooserScreen8Has7RowsEv"));
    //Mod涉及的函数地址
    TakeSunMoneyAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12TakeSunMoneyEii"));
    SeedPacketUpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN10SeedPacket6UpdateEv"));
    GetRefreshTimeSeedAddr = reinterpret_cast<uintptr_t>( dlsym(handle, "_ZN5Plant14GetRefreshTimeE8SeedTypeS0_"));
    AddALadderAddr = reinterpret_cast<uintptr_t>( dlsym(handle, "_ZN5Board10AddALadderEii"));
    AddPlantAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board8AddPlantEii8SeedTypeS0_ib"));
    AddZombieInRowAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board14AddZombieInRowE10ZombieTypeiib"));
    PlantDieAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Plant3DieEv"));
    ProjectileInitializeAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN10Projectile20ProjectileInitializeEiiii14ProjectileType"));
    PlantUpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Plant6UpdateEv"));
    AddSunMoneyAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board11AddSunMoneyEii"));
    MouseDownWithPlantAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board18MouseDownWithPlantEiiii"));
    CanPlantAtAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board10CanPlantAtEii8SeedType"));
    ConvertToFireballAddr = reinterpret_cast<uintptr_t>( dlsym(handle, "_ZN10Projectile17ConvertToFireballEi"));
    ConvertToPeaAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN10Projectile12ConvertToPeaEi"));
    AttachmentDieAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_Z13AttachmentDieR12AttachmentID"));
    LawnApp_PlayFoleyAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp9PlayFoleyE9FoleyType"));
    BoardZombiesWonAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board10ZombiesWonEP6Zombie"));
    DieNoLootAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN6Zombie9DieNoLootEv"));
    ApplyBurnAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN6Zombie9ApplyBurnEv"));
    UpdateConveyorBeltAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8SeedBank18UpdateConveyorBeltEv"));
    DoCheatDialogAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp13DoCheatDialogEv"));
    DoCheatCodeDialogAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp17DoCheatCodeDialogEv"));

    MainMenu_ButtonDepressAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8MainMenu13ButtonDepressEi"));
    MainMenu_InTransitionAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN8MainMenu12InTransitionEv"));
    MainMenu_SetSceneAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN8MainMenu8SetSceneENS_9MenuSceneE"));
    Sexy_ScrollbarWidget_MouseDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN4Sexy15ScrollbarWidget9MouseDownEiiii"));


    ChallengeScreen_AddedToManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN15ChallengeScreen14AddedToManagerEPN4Sexy13WidgetManagerE"));
    ChallengeScreen_RemovedFromManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN15ChallengeScreen18RemovedFromManagerEPN4Sexy13WidgetManagerE"));
    ChallengeScreen_SetScrollTargetAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN15ChallengeScreen15SetScrollTargetEi"));
    ChallengeScreen_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN15ChallengeScreen6UpdateEv"));


    LawnApp_KillChallengeScreenAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN7LawnApp19KillChallengeScreenEv"));
    LawnApp_PreNewGameAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN7LawnApp10PreNewGameE8GameModeb"));
    LawnApp_ShowCreditScreenAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp16ShowCreditScreenEb"));
    LawnApp_KillMainMenuAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp12KillMainMenuEv"));
    LawnApp_KillNewOptionsDialogAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN7LawnApp20KillNewOptionsDialogEv"));
    // Plant_PlantInitializeAddr=reinterpret_cast<uintptr_t>(Plant15PlantInitializeEii8SeedTypeS0_isymbol);
    LawnApp_CanShowStoreAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp12CanShowStoreEv"));
    LawnApp_CanShowAlmanacAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp14CanShowAlmanacEv"));
    SeedChooserScreen_HasPacketAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN17SeedChooserScreen9HasPacketEib"));
    LawnApp_IsScaryPotterLevelAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp18IsScaryPotterLevelEv"));
    LawnApp_IsWhackAZombieLevelAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN7LawnApp19IsWhackAZombieLevelEv"));
    Board_PixelToGridXAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12PixelToGridXEii"));
    Board_PixelToGridYAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12PixelToGridYEii"));
    Coin_DieAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN4Coin3DieEv"));
    Coin_UpadteAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN4Coin6UpdateEv"));
    LawnApp_CanShopLevelAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp12CanShopLevelEv"));
    StoreScreen_SetSelectedSlotAddr = reinterpret_cast<uintptr_t>(dlsym(handle,"_ZN11StoreScreen15SetSelectedSlotEi"));
    StoreScreen_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN11StoreScreen6UpdateEv"));
    Challenge_HeavyWeaponFireAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN9Challenge15HeavyWeaponFireEff"));
//    LawnApp_IsChallengeWithoutSeedBankAddr = reinterpret_cast<uintptr_t>(LawnApp26IsChallengeWithoutSeedBankEvsymbol);
//    GridItem_DrawScaryPotAddr = reinterpret_cast<uintptr_t>(GridItem12DrawScaryPotEPN4Sexy8GraphicsEsymbol);
    Board_MouseHitTestAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board12MouseHitTestEiiP9HitResulti"));
    Board_CanUseGameObjectAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board16CanUseGameObjectE14GameObjectType"));
    Board_DrawZenButtonsAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board14DrawZenButtonsEPN4Sexy8GraphicsE"));
    ZenGarden_RebuildHelpbarAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN9ZenGarden14RebuildHelpbarEb"));
    CreditScreen_RemovedFromManagerAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN12CreditScreen18RemovedFromManagerEPN4Sexy13WidgetManagerE"));
    GamepadControls_OnKeyDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN15GamepadControls9OnKeyDownEN4Sexy7KeyCodeEj"));
    GamepadControls_GetSeedBankAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN15GamepadControls11GetSeedBankEv"));
    Board_ClearCursorAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board11ClearCursorEi"));
    Board_UpdateGameAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board10UpdateGameEv"));
    GamepadControls_DrawAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN15GamepadControls4DrawEPN4Sexy8GraphicsE"));
    CursorObject_DrawAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN12CursorObject4DrawEPN4Sexy8GraphicsE"));
    WaitForSecondPlayerDialog_GameButtonDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN25WaitForSecondPlayerDialog14GameButtonDownEN4Sexy13GamepadButtonEij"));
    Board_MouseDownWithToolAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board17MouseDownWithToolEiii10CursorTypei"));
    Board_ToolHitTestAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board11ToolHitTestEii"));
    LawnApp_ReanimationTryToGetAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN7LawnApp19ReanimationTryToGetE13ReanimationID"));
    Plant_UpdateReanimAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Plant12UpdateReanimEv"));
    Board_CountPlantByTypeAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board16CountPlantByTypeE8SeedType"));
    Board_SetTutorialStateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board16SetTutorialStateE13TutorialState"));
    Board_NewPlantAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board8NewPlantEii8SeedTypeS0_i"));
    Board_GetTopPlantAtAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN5Board13GetTopPlantAtEii13PlantPriority"));
    Sexy_GamepadApp_CheckGamepadAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN4Sexy10GamepadApp12CheckGamepadEv"));
    Sexy_GamepadApp_HasGamepadAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN4Sexy10GamepadApp10HasGamepadEv"));
    Zombie_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN6Zombie6UpdateEv"));
    WaitForSecondPlayerDialog_KeyDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN25WaitForSecondPlayerDialog7KeyDownEN4Sexy7KeyCodeE"));
    SeedChooserScreen_SeedChooserScreenAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN17SeedChooserScreenC2Eb"));
    VSSetupMenu_GameButtonDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN11VSSetupMenu14GameButtonDownEN4Sexy13GamepadButtonEij"));
    VSSetupMenu_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN11VSSetupMenu6UpdateEv"));
    GamepadControls_OnButtonDownAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN15GamepadControls12OnButtonDownEiij"));
    Coin_CollectAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN4Coin7CollectEi"));
    VSResultsMenu_OnExitAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN13VSResultsMenu6OnExitEv"));
    VSResultsMenu_UpdateAddr = reinterpret_cast<uintptr_t>(dlsym(handle, "_ZN13VSResultsMenu6UpdateEv"));
}


#endif //PVZ_TV_1_1_5_HOOKADDR_H
