import com.hb.pocket.Center;
import com.hb.pocket.test.SubscriptionOne;
import com.hb.pocket.test.SubscriptionTwo;
import com.hb.pocket.test.login.LoginInfo;
import com.hb.utils.log.MyLog;

/**
 * Created by hb on 03/07/2018.
 */
public class Main {

    private static String TAG = Main.class.getSimpleName();

    public static void main(String[] args) {
        MyLog.d(TAG, "Hello Pocket Message Queue!");

        SubscriptionOne subscriptionOne = new SubscriptionOne();
        SubscriptionTwo subscriptionTwo = new SubscriptionTwo();

        LoginInfo loginInfo = new LoginInfo(false);
        Center.getInstance().postAllSubscription(loginInfo);

        loginInfo.setSuccess(true);
        Center.getInstance().post(loginInfo, SubscriptionTwo.class);

        subscriptionOne.unregister();
        subscriptionTwo.unregister();
    }

}
