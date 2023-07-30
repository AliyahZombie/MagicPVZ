//
// Created by Homura on 2023/5/18.
//

#ifndef PVZ_TV_TOUCH_STEPONE_PATCH_H
#define PVZ_TV_TOUCH_STEPONE_PATCH_H

#define targetLibName "libGameMain.so"

struct Game_Patches {
    MemoryPatch RemoveSessionTaskFailedDialog,
    RepairZombiesWon,
    RepairShopA,
    RepairShopB,
    ShowShovel,
//    BloatZombie,
    ManualCollection;

} GamePatches;

bool IsPatched = false;
bool enableManualCollect = false;

void StepOnePatchGame(){
    if (!IsPatched) {
        GamePatches.RemoveSessionTaskFailedDialog = MemoryPatch::createWithHex(targetLibName,string2Offset("0x2B2B74"),"70 47");
        GamePatches.RemoveSessionTaskFailedDialog.Modify();
        GamePatches.RepairZombiesWon = MemoryPatch::createWithHex(targetLibName,string2Offset("0x1B8E4C"),"04 E0");
        GamePatches.RepairZombiesWon.Modify();
        GamePatches.RepairShopA = MemoryPatch::createWithHex(targetLibName,string2Offset("0x1C3B06"),"05 E0");
        GamePatches.RepairShopA.Modify();
        GamePatches.RepairShopB = MemoryPatch::createWithHex(targetLibName,string2Offset("0x1C3C6C"),"06 E0");
        GamePatches.RepairShopB.Modify();
        GamePatches.ShowShovel = MemoryPatch::createWithHex(targetLibName,string2Offset("0x1C23D2"),"05 2A 00 F3 ED 81");
        GamePatches.ShowShovel.Modify();
//        GamePatches.BloatZombie = MemoryPatch::createWithHex(targetLibName,string2Offset("0x1B7532"),"4F F0 02 02");
//        GamePatches.BloatZombie.Modify();
        GamePatches.ManualCollection = MemoryPatch::createWithHex(targetLibName,string2Offset("0x19B15A"),"00 BF 00 BF");
        if (enableManualCollect){
            GamePatches.ManualCollection.Modify();
        }
        IsPatched = true;
    }
}




#endif //PVZ_TV_TOUCH_STEPONE_PATCH_H
