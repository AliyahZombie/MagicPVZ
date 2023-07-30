package com.transmension.mobile;

import android.app.Activity;
import android.text.TextUtils;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public interface GameCenter {
    public static final int ANTIADDICTION_DISABLED = 0;
    public static final int ANTIADDICTION_ENABLED = 1;
    public static final int ANTIADDICTION_UNKNOWN = -1;
    public static final int EVENT_INIT_FINISHED = 11;
    public static final int EVENT_INVITE_FRIEND = 7;
    public static final int EVENT_LEAVE = 3;
    public static final int EVENT_LOGIN = 1;
    public static final int EVENT_LOGOUT = 2;
    public static final int EVENT_MESSAGE = 10;
    public static final int EVENT_PAY = 8;
    public static final int EVENT_PAY_INFO = 9;
    public static final int EVENT_PRODUCTS_RECEIVED = 12;
    public static final int EVENT_QUERY_ANTIADDICTION = 4;
    public static final int EVENT_QUERY_FRIENDS = 5;
    public static final int EVENT_QUERY_INVITABLE_FRIENDS = 6;
    public static final int FEATURE_ADVERTISEMENT = 524288;
    public static final int FEATURE_ANTIADDICTION = 32;
    public static final int FEATURE_APPUPDATE = 8;
    public static final int FEATURE_EVENT_REPORT = 16384;
    public static final int FEATURE_EXIT = 64;
    public static final int FEATURE_FRIEND = 256;
    public static final int FEATURE_GLOBAL_LOGIN = 32768;
    public static final int FEATURE_GUEST_LOGIN = 8192;
    public static final int FEATURE_INITIAL_LOGIN_REQUIRED = 65536;
    public static final int FEATURE_INVITEFRIEND = 512;
    public static final int FEATURE_LEADERBOARD = 2048;
    public static final int FEATURE_MESSAGE = 4096;
    public static final int FEATURE_MORE_GAMES = 262144;
    public static final int FEATURE_MULTI_USER_TYPES = 1048576;
    public static final int FEATURE_NULL = 0;
    public static final int FEATURE_ONETIMETOKEN = 4;
    public static final int FEATURE_PURCHASE = 16;
    public static final int FEATURE_RECHARGE = 2;
    public static final int FEATURE_SCORE_REPORT = 1024;
    public static final int FEATURE_TOOLBAR = 128;
    public static final int FEATURE_USERMANAGMENT = 1;
    public static final int FEATURE_USER_CENTER = 131072;
    public static final int GUEST_LOGGEDIN = 2;
    public static final int LOGGEDIN = 1;
    public static final int LOGIN_ALLOW_GUEST = 1;
    public static final String MESSAGE_TYPE_CHAT = "Chat";
    public static final String MESSAGE_TYPE_CUSTOM = "Custom";
    public static final String MESSAGE_TYPE_GIFT = "Gift";
    public static final int NOT_LOGGEDIN = 0;
    public static final int PAY_CANCELED = -1;
    public static final int PAY_FAILED = 1;
    public static final int PAY_INPROGRESS = -2;
    public static final int PAY_SUCCESS = 0;
    public static final int PURCHASE_CARRIER = 2;
    public static final int PURCHASE_NO_REGISTER = 4;
    public static final int PURCHASE_OFFLINE = 1;
    public static final String VIEW_AD_BANNER = "AdBanner";
    public static final String VIEW_AD_POPUP = "AdPopup";
    public static final String VIEW_AD_TEXT = "AdText";
    public static final String VIEW_DEFAULT = "";
    public static final String VIEW_EXIT = "Exit";
    public static final String VIEW_INVITEFRIEND = "InviteFriend";
    public static final String VIEW_LEADERBOARD = "Leaderboard";
    public static final String VIEW_MOREGAMES = "MoreGames";
    public static final String VIEW_NAMEREGISTER = "NameRegister";
    public static final String VIEW_TOOLBAR = "ToolBar";

    /* loaded from: classes.dex */
    public static class Event {
        public String event;
        public String ext1;
        public String ext2;
    }

    /* loaded from: classes.dex */
    public static class Score {
        public String context;
        public String ext1;
        public String ext2;
        public String id;
        public String score;
        public String userId;
        public String userName;
    }

    boolean addFriends();

    boolean canMakePurchase();

    String generateOrderId();

    int getAntiAddictionState();

    String getAuthToken();

    String getCurrency();

    int getFeatures();

    List<Friend> getFriends();

    List<Friend> getInvitableFriends();

    int getLoginState();

    String getName();

    List<Product> getProducts();

    String getProperty(String str);

    String getProxyUrl(String str);

    int getPurchaseFeatures();

    String getSessionId();

    String getUserAvatar();

    String getUserId();

    String getUserName();

    String getUserTag();

    boolean hasProperty(String str);

    boolean hide(String str);

    boolean inviteFriend(Friend friend, String str, String str2);

    boolean isAvailabe();

    boolean isLoggedIn();

    boolean isQueryingProducts();

    boolean login();

    boolean login(int i);

    boolean logout();

    void onDestroy();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    boolean prepare();

    boolean purchase(Payment payment);

    boolean queryAntiAddiction();

    boolean queryFriends();

    boolean queryInvitableFriends();

    void queryProducts();

    boolean recharge(RechargeArgs rechargeArgs);

    boolean register(String str);

    boolean reportEvent(Event event);

    boolean reportScore(Score score);

    boolean sendMessage(Message message);

    void setListener(Listener listener);

    boolean setProperty(String str, String str2);

    boolean show(String str);

    boolean switchAccount();

    /* loaded from: classes.dex */
    public static abstract class Listener {
        public abstract void onEvent(Activity activity, GameCenter gameCenter, int i, int i2, String str);

        public void onEvent(Activity activity, GameCenter gameCenter, int event) {
            onEvent(activity, gameCenter, event, 0, null);
        }

        public void onLogin(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 1, 0, null);
        }

        public void onLogout(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 2, 0, null);
        }

        public void onLeave(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 3, 0, null);
        }

        public void onQueryAntiAddiction(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 4, 0, null);
        }

        public void onQueryFriends(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 5, 0, null);
        }

        public void onQueryInvitableFriends(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 6, 0, null);
        }

        public void onInviteFriend(Activity activity, GameCenter gameCenter, int status) {
            onEvent(activity, gameCenter, 7, status, null);
        }

        public void onPay(Activity activity, GameCenter gameCenter, int status) {
            onEvent(activity, gameCenter, 8, status, null);
        }

        public void onPay(Activity activity, GameCenter gameCenter, int status, String extras) {
            onEvent(activity, gameCenter, 8, status, extras);
        }

        public void onMessage(Activity activity, GameCenter gameCenter, int status, Message message) {
            onEvent(activity, gameCenter, 10, status, message.toJSONString());
        }

        public void onInitFinished(Activity activity, GameCenter gameCenter) {
            onEvent(activity, gameCenter, 11);
        }

        public void onProductsReceived(Activity activity, GameCenter gameCenter, int status) {
            onEvent(activity, gameCenter, 12, status, null);
        }
    }

    /* loaded from: classes.dex */
    public static class RechargeArgs {
        public float amount;
        public String description;
        public String orderId;
        public String userId;
        public String userName;
        public int zoneId;

        public RechargeArgs(String orderId, float amount, String description) {
            this.orderId = orderId;
            this.amount = amount;
            this.description = description;
            this.zoneId = 0;
            this.userId = "";
            this.userName = "";
        }

        public RechargeArgs(String orderId, float amount, int zoneId, String description) {
            this.orderId = orderId;
            this.amount = amount;
            this.description = description;
            this.zoneId = zoneId;
            this.userId = "";
            this.userName = "";
        }
    }

    /* loaded from: classes.dex */
    public static class Product {
        public float amount;
        public String currency;
        public String description;
        public String detail;
        public String ext1;
        public String ext2;
        public Map<String, String> extras;
        public int features;
        public String identifier;
        public float price;
        public int priority;
        public String source;

        String getIdentifier() {
            return this.identifier;
        }

        float getPrice() {
            return this.price;
        }

        String getDetail() {
            return this.detail;
        }

        String getDescription() {
            return this.description;
        }

        String getCurrency() {
            return this.currency;
        }

        public Product() {
            this.price = 0.0f;
            this.amount = 1.0f;
            this.identifier = "";
            this.detail = "";
            this.description = "";
            this.currency = "";
            this.ext1 = "";
            this.ext2 = "";
            this.source = "";
            this.features = 0;
            this.priority = 0;
        }

        public Product(String id, float price, String detail, String desc) {
            this.price = price;
            this.amount = 1.0f;
            this.identifier = id;
            this.detail = detail;
            this.description = desc;
            this.ext1 = "";
            this.ext2 = "";
            this.source = "";
            this.features = 0;
            this.priority = 0;
        }

        public Product(String id, float price, String detail, String desc, String currency) {
            this.price = price;
            this.amount = 1.0f;
            this.identifier = id;
            this.detail = detail;
            this.description = desc;
            this.currency = currency;
            this.ext1 = "";
            this.ext2 = "";
            this.source = "";
            this.features = 0;
            this.priority = 0;
        }

        public Product(String id, float price, float amount, String detail, String desc, String currency) {
            this.price = price;
            this.amount = amount;
            this.identifier = id;
            this.detail = detail;
            this.description = desc;
            this.currency = currency;
            this.ext1 = "";
            this.ext2 = "";
            this.source = "";
            this.features = 0;
            this.priority = 0;
        }
    }

    /* loaded from: classes.dex */
    public static class Payment {
        public float amount;
        public String channel;
        public String description;
        public String identifier;
        public String orderId;
        public String source;
        public Object userData;
        public String userId;
        public String userName;
        public int zoneId;

        public Payment() {
            this.amount = 0.0f;
            this.zoneId = 0;
            this.identifier = "";
            this.description = "";
            this.source = "";
            this.userData = null;
            this.channel = "";
        }

        public Payment(String id, float amount, String desc, int zoneId) {
            this.amount = amount;
            this.identifier = id;
            this.zoneId = zoneId;
            this.description = desc;
            this.userId = "";
            this.userName = "";
            this.source = "";
            this.userData = null;
            this.channel = "";
        }

        public Payment(String id, float amount, String desc, int zoneId, String userId, String userName, String orderId) {
            this.amount = amount;
            this.identifier = id;
            this.zoneId = zoneId;
            this.description = desc;
            this.userId = userId;
            this.userName = userName;
            this.orderId = orderId;
            this.source = "";
            this.userData = null;
            this.channel = "";
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public float getAmount() {
            return this.amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getZoneId() {
            return this.zoneId;
        }

        public void setZoneId(int zoneId) {
            this.zoneId = zoneId;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getOrderId() {
            return this.orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Object getUserData() {
            return this.userData;
        }

        public void setUserData(Object userData) {
            this.userData = userData;
        }
    }

    /* loaded from: classes.dex */
    public static class Friend {
        public String ext1;
        public String ext2;
        public String ext3;
        public String ext4;
        public boolean friend;
        public boolean invited;
        public boolean player;
        public String name = "";
        public String id = "";
        public String phone = "";
        public String email = "";
        public String avatar = "";
        public String source = "";

        public Friend() {
            this.player = false;
            this.invited = false;
            this.friend = false;
            this.player = false;
            this.invited = false;
            this.friend = false;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSource() {
            return this.source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public boolean isPlayer() {
            return this.player;
        }

        public void setPlayer(boolean player) {
            this.player = player;
        }

        public boolean isInvited() {
            return this.invited;
        }

        public void setInvited(boolean invited) {
            this.invited = invited;
        }

        public boolean isFriend() {
            return this.friend;
        }

        public void setFriend(boolean friend) {
            this.friend = friend;
        }

        public String getExt1() {
            return this.ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getExt2() {
            return this.ext2;
        }

        public void setExt2(String ext2) {
            this.ext2 = ext2;
        }

        public String getExt3() {
            return this.ext3;
        }

        public void setExt3(String ext3) {
            this.ext3 = ext3;
        }

        public String getExt4() {
            return this.ext4;
        }

        public void setExt4(String ext4) {
            this.ext4 = ext4;
        }
    }

    /* loaded from: classes.dex */
    public static class Message {
        public String ext1;
        public String ext2;
        public String id;
        public String message;
        public String type;
        public String userId;
        public String userName;

        public static Message parseJSONString(String str) {
            Message result = new Message();
            if (!TextUtils.isEmpty(str)) {
                try {
                    JSONObject jsonObj = new JSONObject(str);
                    if (jsonObj.has("user_name")) {
                        result.userName = jsonObj.getString("user_name");
                    }
                    if (jsonObj.has("user_id")) {
                        result.userId = jsonObj.getString("user_id");
                    }
                    if (jsonObj.has("id")) {
                        result.id = jsonObj.getString("id");
                    }
                    if (jsonObj.has("type")) {
                        result.type = jsonObj.getString("type");
                    }
                    if (jsonObj.has("message")) {
                        result.message = jsonObj.getString("message");
                    }
                    if (jsonObj.has("ext1")) {
                        result.ext1 = jsonObj.getString("ext1");
                    }
                    if (jsonObj.has("ext2")) {
                        result.ext2 = jsonObj.getString("ext2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        public String toJSONString() {
            JSONObject obj = new JSONObject();
            try {
                if (!TextUtils.isEmpty(this.userName)) {
                    obj.put("user_name", this.userName);
                }
                if (!TextUtils.isEmpty(this.userId)) {
                    obj.put("user_id", this.userId);
                }
                if (!TextUtils.isEmpty(this.id)) {
                    obj.put("id", this.id);
                }
                if (!TextUtils.isEmpty(this.type)) {
                    obj.put("type", this.type);
                }
                if (!TextUtils.isEmpty(this.message)) {
                    obj.put("message", this.message);
                }
                if (!TextUtils.isEmpty(this.ext1)) {
                    obj.put("ext1", this.ext1);
                }
                if (!TextUtils.isEmpty(this.ext2)) {
                    obj.put("ext2", this.ext2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj.toString();
        }
    }
}