package com.preciousumeha.globallotto;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Constants {
    public static  final String table_results = "table_results";
    public static final String url = "https://www.tossymobile.com/global/mobile/";
    public static final String url_create_account = "https://www.tossymobile.com/global/mobile/register.php";
    public static final String url_verify = "https://www.tossymobile.com/global/mobile/verify.php";
    public static final String url_signin = "https://www.tossymobile.com/global/mobile/login.php";
    public static final String url_share_funds = "https://www.tossymobile.com/global/mobile/share_funds.php";
    public static final String url_my_bank = "https://www.tossymobile.com/global/mobile/my_bank.php";
    public static final String url_fund_history = "https://www.tossymobile.com/global/mobile/fund_history.php";
    public static final String url_share_fund_history = "https://www.tossymobile.com/global/mobile/share_history.php";
    public static final String url_notify = "https://www.tossymobile.com/global/mobile/notifications.php";
    public static final String url_add_bank = "https://www.tossymobile.com/global/mobile/add_bank.php";
    public static final String url_check_game = url+"lotto_history.php";
    public static final String url_withdraw = url+"withdrawal.php";
    public static final String url_withdraw_history = url+"withdrawal_history.php";
    public static final String url_play_game = url +"play_game.php";
    public static final String url_my_bal = url+"my_bal";
    public static final String url_agent = url +"agent_status.php";
    public static final String url_game_check = url +"check_played_game.php";
    public static final String url_agent_play = url + "play_game_agent.php";
    public static String url_change_password = url+"change_pass.php";

    public static String ud_password = "opass";
    public static final String ud_user = "ouser";
    public static final String verifycode = "vcode";
    public static final String phone = "phone";
    public static final String amount = "amt";
    public static final String status = "status";
    public static final String TAG = "Global-lotto";
    //Preference
    public static final String preference_user_status = "user_status";
    public static final String preference_user_detail = "detail_status";
    public static final String preference_permission = "permissionStatus";
    /*
    *User detail -- ud
     */
    public static final String ud_user_id = "user_id";
    public static final String ud_name = "name";
    public static final String ud_email = "email";
    public static final String ud_wallet = "wallet";
    public static final String ud_balance = "balance";
    public static final String ud_phone = "phone";
    public static final String ud_token = "token";
    public static final String ud_agent = "agent";
    public static final String mdate = "mdate";

    public static void newToast(String text, Context context){
        LinearLayout layout=new LinearLayout(context);
        layout.setBackgroundResource(R.drawable.toast);
        layout.setPadding(10,10,10,10);
        TextView tv=new TextView(context);
        tv.setTextColor( context.getResources().getColor(R.color.color_white));
        tv.setTypeface(Typeface.SANS_SERIF);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        layout.addView(tv);
        Toast toast=new Toast(context);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    public static class stopCopy implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }




    // ArrayList<String> games = new ArrayList<>();
   // String[] games = new String[]{}
}
