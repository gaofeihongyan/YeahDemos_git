package spider.szc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class ProgressAsynTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private Context context;
	private String msg;
	private ProgressDialog mProgressDlg;
	
	public ProgressAsynTask(Context context, String msg){
		this.context = context;
		this.msg = msg;
	}
	
	public ProgressAsynTask(Context context, int id){
		this.context = context;
		if(id == 0){
			this.msg = "";
		}else{
			this.msg = context.getString(id);
		}
	}
	
	@Override
	protected void onPreExecute() {
		mProgressDlg = new ProgressDialog(context);
		mProgressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDlg.setIndeterminate(false);
		mProgressDlg.setCanceledOnTouchOutside(false);
		mProgressDlg.setCancelable(false);
		mProgressDlg.setMessage(msg);
		mProgressDlg.show();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		mProgressDlg.dismiss();
		super.onPostExecute(result);
	}
}
