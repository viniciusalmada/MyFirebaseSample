package br.almadaapps.myfirebasesample;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vinicius on 28/08/16.
 */

public abstract class CommonActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;

    protected void showProgressDialog(){

        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected abstract void initViews();

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
