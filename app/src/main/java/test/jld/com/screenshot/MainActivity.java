package test.jld.com.screenshot;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.io.FileOutputStream;

public class MainActivity extends Activity {

    private VideoView myMp4;
    private TextView textView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMp4 = (VideoView) findViewById(R.id.videoView);
        textView = (TextView) findViewById(R.id.Text);
        button = (Button) findViewById(R.id.but);

        myMp4.setVideoPath("/mnt/extsd/jld/temp/385.mp4");

        MediaController mediaco = new MediaController(MainActivity.this);
        mediaco.setVisibility(View.GONE);
        myMp4.setMediaController(mediaco);
        mediaco.setMediaPlayer(myMp4);
        //让VideiView获取焦点
        myMp4.requestFocus();

        myMp4.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        myMp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myMp4.start();
            }
        });
        myMp4.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
// 设置当前播放的位置
                //myMp4.play(progress);
                //mp.isPlaying = false;
                return true;//如果设置true就可以防止他弹出错误的提示框！
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ViewGroup v = (ViewGroup) MainActivity.this.getWindow().getDecorView();
                View view = v.getRootView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap newBitmap = view.getDrawingCache();
                String fnm="/mnt/extsd/jld/"+"test.png";


                Bitmap bitmap = null;
                String filePath = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                        filePath = "/mnt/extsd/jld/temp/385.mp4";

                        retriever.setDataSource(filePath);

                        bitmap = retriever.getFrameAtTime(
//                                Long.parseLong(currentVideoTime),
//                            myMp4.getCurrentPosition() * 1000,
//                                MediaMetadataRetriever.OPTION_CLOSEST
                                                                  );
                        bitmap = zoomImg(bitmap,myMp4.getWidth(),myMp4.getHeight());
                        Canvas cv = new Canvas(newBitmap);
                        cv.drawBitmap(bitmap,16,16, null);//在 0，0坐标开始画入bg
                        cv.save(Canvas.ALL_SAVE_FLAG);//保存
                        cv.restore();//存储
//                        LogMessage.write(1,"截取视频成功"+videoName+"截屏时间"+
////                                        Long.parseLong(currentVideoTime)
//                                    myMp4.getCurrentPosition()*1000
//                        );
                    if (newBitmap != null) {
                        try {
                            Bitmap bitmapSave    = BitmapUtil.comp(newBitmap);
                            FileOutputStream out = new FileOutputStream(fnm);
                            bitmapSave.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();

                            textView.setText("截屏成功时间:"
//                                    + myMp4.getCurrentPosition() * 1000
                                    +"内容"+bitmap.toString());
                        } catch (Exception ex) {
//                            LogMessage.write(1, ex.getMessage() + ": error in take screenshot");
                        }
                    } else
//                        LogMessage.write(1, "error in take screen shot,empty bitmap");
                    return;
                } catch (Exception e) {
                    Log.e("PowerMediaProject","fail to videoScreen"+e.getMessage());
                }
            }
        });
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
