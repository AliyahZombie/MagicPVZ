package com.transmension.mobile;

import android.app.Activity;
import com.transmension.mobile.GameCenter;
import java.util.List;

/* loaded from: classes.dex */
public class GameCenterFactory {

    /* loaded from: classes.dex */
    public static class DefaultGameCenter implements GameCenter {
        @Override // com.transmension.mobile.GameCenter
        public boolean prepare() {
            return true;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean isAvailabe() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public String getName() {
            return "Default";
        }

        @Override // com.transmension.mobile.GameCenter
        public int getFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean isLoggedIn() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean login() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean login(int flags) {
            return login();
        }

        @Override // com.transmension.mobile.GameCenter
        public int getLoginState() {
            return isLoggedIn() ? 1 : 0;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean logout() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean switchAccount() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean register(String tip) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public String getSessionId() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getAuthToken() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getUserName() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getUserId() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getUserTag() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getUserAvatar() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean show(String what) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean hide(String what) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean recharge(GameCenter.RechargeArgs args) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public String getCurrency() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public void setListener(GameCenter.Listener listener) {
        }

        @Override // com.transmension.mobile.GameCenter
        public void onStart() {
        }

        @Override // com.transmension.mobile.GameCenter
        public void onPause() {
        }

        @Override // com.transmension.mobile.GameCenter
        public void onResume() {
        }

        @Override // com.transmension.mobile.GameCenter
        public void onStop() {
        }

        @Override // com.transmension.mobile.GameCenter
        public void onDestroy() {
        }

        @Override // com.transmension.mobile.GameCenter
        public String generateOrderId() {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean canMakePurchase() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public List<GameCenter.Product> getProducts() {
            return null;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean purchase(GameCenter.Payment payment) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public void queryProducts() {
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean isQueryingProducts() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public int getPurchaseFeatures() {
            return 0;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean setProperty(String key, String value) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public String getProperty(String key) {
            if (key.equals("UserName")) {
                return getUserName();
            }
            if (key.equals("UserId")) {
                return getUserName();
            }
            if (key.equals("SessionId")) {
                return getSessionId();
            }
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public String getProxyUrl(String url) {
            return "";
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean hasProperty(String key) {
            return key.equals("UserName") || key.equals("UserId") || key.equals("SessionId");
        }

        @Override // com.transmension.mobile.GameCenter
        public int getAntiAddictionState() {
            return -1;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean queryAntiAddiction() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public List<GameCenter.Friend> getFriends() {
            return null;
        }

        @Override // com.transmension.mobile.GameCenter
        public List<GameCenter.Friend> getInvitableFriends() {
            return null;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean inviteFriend(GameCenter.Friend invitable, String message, String optional) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean queryFriends() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean queryInvitableFriends() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean addFriends() {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean reportScore(GameCenter.Score score) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean sendMessage(GameCenter.Message message) {
            return false;
        }

        @Override // com.transmension.mobile.GameCenter
        public boolean reportEvent(GameCenter.Event event) {
            return false;
        }
    }

    public GameCenter create(Activity activity) {
        return new DefaultGameCenter();
    }
}