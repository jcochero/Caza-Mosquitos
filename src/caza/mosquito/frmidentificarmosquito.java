package caza.mosquito;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class frmidentificarmosquito extends Activity implements B4AActivity{
	public static frmidentificarmosquito mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmidentificarmosquito");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmidentificarmosquito).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmidentificarmosquito");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmidentificarmosquito", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmidentificarmosquito) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmidentificarmosquito) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return frmidentificarmosquito.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (frmidentificarmosquito) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmidentificarmosquito) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            frmidentificarmosquito mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmidentificarmosquito) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.phone.Phone _vvvvvvv6 = null;
public static int _vvvvvvv7 = 0;
public static String _vvvvvvv0 = "";
public static String _vvvvvvvv1 = "";
public static String _vvvvvvvv2 = "";
public static String _vvvvvvvv3 = "";
public flm.b4a.scrollview2d.ScrollView2DWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncontinuar = null;
public static int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lblinstrucciones = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public de.amberhome.viewpager.AHPageContainer _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public de.amberhome.viewpager.AHViewPager _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfondo = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public flm.b4a.gesturedetector.GestureDetectorForB4A _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public static float _vvvvvvvvvvvvvvvvvvvvvvvvvvv0 = 0f;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblslide = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnenviar = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgopcion1_1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgopcion2_1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgopcion4_1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgopcion3_1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinpregunta1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblrespuesta = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinpregunta2 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinpregunta3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpregunta2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpregunta3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnokseleccionmosquito = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = "";
public anywheresoftware.b4a.objects.drawable.ColorDrawable _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spncabeza = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spntorax = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnabdomen = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnpatas = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spnespecie = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquito = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrpartes = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcabeza = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpatas = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblabdomen = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltorax = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnnose = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblespecie = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _pgbculex = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _pgbalbifasciatus = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _pgbalbopictus = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _pgbaegipty = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblhighlight = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.starter _vvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomofotos _vvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmlogin _vvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.dbutils _vvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.downloadservice _vvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.firebasemessaging _vvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmabout _vvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmaprender _vvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.frmcamara _vvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomotransmiten _vvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmcomovemos_app _frmcomovemos_app = null;
public caza.mosquito.frmcomovemos_enfermedades _frmcomovemos_enfermedades = null;
public caza.mosquito.frmcomovemos_porqueexiste _frmcomovemos_porqueexiste = null;
public caza.mosquito.frmdatosanteriores _vvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.frmdondecria _vvvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.frmeditprofile _vvvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.frmelmosquito _vvvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmenfermedades _vvvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmfotos _vvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.frmfotoscriadero _vvvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frminfografias_main _frminfografias_main = null;
public caza.mosquito.frminstrucciones _vvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.frmlocalizacion _vvvvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.frmmapa _vvvvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.frmpoliticadatos _vvvvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmquehacer _vvvvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.httputils2service _vvvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.multipartpost _vvvvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.register _vvvvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.uploadfiles _vvvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.utilidades _vvvvvvvvvvvvvvvvvvvvv7 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 169;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 170;BA.debugLine="p.SetScreenOrientation(0)";
_vvvvvvv6.SetScreenOrientation(processBA,(int) (0));
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 190;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 184;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 185;BA.debugLine="Activity.LoadLayout(\"laySeleccionMosquito\")";
mostCurrent._activity.LoadLayout("laySeleccionMosquito",mostCurrent.activityBA);
 //BA.debugLineNum = 186;BA.debugLine="CargarEspecies";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 187;BA.debugLine="Msgbox2(\"Observa diferentes esquemas del patrón d";
anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Observa diferentes esquemas del patrón de coloración de los mosquitos y elige aquellos que se parecen más al tuyo!"),BA.ObjectToCharSequence("¿Cómo es tu mosquito?"),"Ok!","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"iconsmall.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _btnabdomen1_click() throws Exception{
 //BA.debugLineNum = 507;BA.debugLine="Sub btnAbdomen1_Click";
 //BA.debugLineNum = 508;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"a";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen1.png").getObject()));
 //BA.debugLineNum = 509;BA.debugLine="spnAbdomen.SelectedIndex = 1";
mostCurrent._spnabdomen.setSelectedIndex((int) (1));
 //BA.debugLineNum = 510;BA.debugLine="imgAbdomen.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 511;BA.debugLine="imgAbdomen.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 512;BA.debugLine="imgAbdomen.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 513;BA.debugLine="imgAbdomen.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 514;BA.debugLine="btnAbdomen1.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 515;BA.debugLine="btnAbdomen2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 516;BA.debugLine="btnAbdomen3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 518;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 519;BA.debugLine="End Sub";
return "";
}
public static String  _btnabdomen2_click() throws Exception{
 //BA.debugLineNum = 520;BA.debugLine="Sub btnAbdomen2_Click";
 //BA.debugLineNum = 521;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"a";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen2.png").getObject()));
 //BA.debugLineNum = 522;BA.debugLine="spnAbdomen.SelectedIndex = 2";
mostCurrent._spnabdomen.setSelectedIndex((int) (2));
 //BA.debugLineNum = 523;BA.debugLine="imgAbdomen.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 524;BA.debugLine="imgAbdomen.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 525;BA.debugLine="imgAbdomen.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 526;BA.debugLine="imgAbdomen.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 527;BA.debugLine="btnAbdomen2.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 528;BA.debugLine="btnAbdomen1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 529;BA.debugLine="btnAbdomen3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 531;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 532;BA.debugLine="End Sub";
return "";
}
public static String  _btnabdomen3_click() throws Exception{
 //BA.debugLineNum = 533;BA.debugLine="Sub btnAbdomen3_Click";
 //BA.debugLineNum = 534;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"a";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen3.png").getObject()));
 //BA.debugLineNum = 535;BA.debugLine="spnAbdomen.SelectedIndex = 3";
mostCurrent._spnabdomen.setSelectedIndex((int) (3));
 //BA.debugLineNum = 536;BA.debugLine="imgAbdomen.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 537;BA.debugLine="imgAbdomen.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 538;BA.debugLine="imgAbdomen.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 539;BA.debugLine="imgAbdomen.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 540;BA.debugLine="btnAbdomen3.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 541;BA.debugLine="btnAbdomen1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 542;BA.debugLine="btnAbdomen2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 544;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 545;BA.debugLine="End Sub";
return "";
}
public static String  _btncabeza1_click() throws Exception{
 //BA.debugLineNum = 867;BA.debugLine="Sub btnCabeza1_Click";
 //BA.debugLineNum = 868;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"Ca";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Cabeza1.png").getObject()));
 //BA.debugLineNum = 869;BA.debugLine="spnCabeza.SelectedIndex = 1";
mostCurrent._spncabeza.setSelectedIndex((int) (1));
 //BA.debugLineNum = 870;BA.debugLine="imgCabeza.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 871;BA.debugLine="imgCabeza.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 872;BA.debugLine="imgCabeza.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 873;BA.debugLine="imgCabeza.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 874;BA.debugLine="btnCabeza1.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 875;BA.debugLine="btnCabeza2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 876;BA.debugLine="btnCabeza3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 878;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 879;BA.debugLine="End Sub";
return "";
}
public static String  _btncabeza2_click() throws Exception{
 //BA.debugLineNum = 880;BA.debugLine="Sub btnCabeza2_Click";
 //BA.debugLineNum = 881;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"Ca";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Cabeza2.png").getObject()));
 //BA.debugLineNum = 882;BA.debugLine="spnCabeza.SelectedIndex = 2";
mostCurrent._spncabeza.setSelectedIndex((int) (2));
 //BA.debugLineNum = 883;BA.debugLine="imgCabeza.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 884;BA.debugLine="imgCabeza.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 885;BA.debugLine="imgCabeza.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 886;BA.debugLine="imgCabeza.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 887;BA.debugLine="btnCabeza1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 888;BA.debugLine="btnCabeza2.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 889;BA.debugLine="btnCabeza3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 891;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 892;BA.debugLine="End Sub";
return "";
}
public static String  _btncabeza3_click() throws Exception{
 //BA.debugLineNum = 893;BA.debugLine="Sub btnCabeza3_Click";
 //BA.debugLineNum = 894;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"Ca";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Cabeza3.png").getObject()));
 //BA.debugLineNum = 895;BA.debugLine="spnCabeza.SelectedIndex = 3";
mostCurrent._spncabeza.setSelectedIndex((int) (3));
 //BA.debugLineNum = 896;BA.debugLine="imgCabeza.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 897;BA.debugLine="imgCabeza.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 898;BA.debugLine="imgCabeza.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 899;BA.debugLine="imgCabeza.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 900;BA.debugLine="btnCabeza1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 901;BA.debugLine="btnCabeza2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 902;BA.debugLine="btnCabeza3.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 904;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 905;BA.debugLine="End Sub";
return "";
}
public static String  _btncambiarfoto_click() throws Exception{
 //BA.debugLineNum = 1156;BA.debugLine="Sub btnCambiarFoto_Click";
 //BA.debugLineNum = 1158;BA.debugLine="End Sub";
return "";
}
public static String  _btnmasinfo_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _speciesmap = null;
 //BA.debugLineNum = 1206;BA.debugLine="Sub btnMasInfo_Click";
 //BA.debugLineNum = 1207;BA.debugLine="btnContinuar.Visible = False";
mostCurrent._btncontinuar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1208;BA.debugLine="btnVerMiFoto.Visible = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1209;BA.debugLine="opcionElegida = Pager.CurrentPage +1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCurrentPage()+1);
 //BA.debugLineNum = 1211;BA.debugLine="Dim speciesMap As Map";
_speciesmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1212;BA.debugLine="speciesMap.Initialize";
_speciesmap.Initialize();
 //BA.debugLineNum = 1213;BA.debugLine="speciesMap = DBUtils.ExecuteMap(Starter.speciesDB";
_speciesmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._vv1 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM especies WHERE Id=?",new String[]{BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6+1)});
 //BA.debugLineNum = 1214;BA.debugLine="If speciesMap = Null Or speciesMap.IsInitialized";
if (_speciesmap== null || _speciesmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1215;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 1217;BA.debugLine="Activity.LoadLayout(\"layCaracteristicasSp1\")";
mostCurrent._activity.LoadLayout("layCaracteristicasSp1",mostCurrent.activityBA);
 //BA.debugLineNum = 1218;BA.debugLine="lblNombre.Text = speciesMap.Get(\"especie\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setText(BA.ObjectToCharSequence(_speciesmap.Get((Object)("especie"))));
 //BA.debugLineNum = 1219;BA.debugLine="lblDescripcionGeneral.Text = speciesMap.Get(\"gen";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setText(BA.ObjectToCharSequence(_speciesmap.Get((Object)("general"))));
 //BA.debugLineNum = 1221;BA.debugLine="imgHabitat.Bitmap = LoadBitmapSample(File.DirAss";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),BA.ObjectToString(_speciesmap.Get((Object)("habitat"))),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight()).getObject()));
 //BA.debugLineNum = 1222;BA.debugLine="imgDistribucion.Bitmap = LoadBitmapSample(File.D";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),BA.ObjectToString(_speciesmap.Get((Object)("distribucion"))),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight()).getObject()));
 //BA.debugLineNum = 1223;BA.debugLine="btnVolver.Visible = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1229;BA.debugLine="End Sub";
return "";
}
public static String  _btnnose_click() throws Exception{
 //BA.debugLineNum = 908;BA.debugLine="Sub btnNoSe_Click";
 //BA.debugLineNum = 909;BA.debugLine="If spnPatas.Visible = True Then";
if (mostCurrent._spnpatas.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 910;BA.debugLine="spnPatas.SelectedIndex = 0";
mostCurrent._spnpatas.setSelectedIndex((int) (0));
 //BA.debugLineNum = 911;BA.debugLine="imgPatas.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 912;BA.debugLine="btnPatas1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 913;BA.debugLine="btnPatas2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 915;BA.debugLine="If spnAbdomen.Visible = True Then";
if (mostCurrent._spnabdomen.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 916;BA.debugLine="spnAbdomen.SelectedIndex = 0";
mostCurrent._spnabdomen.setSelectedIndex((int) (0));
 //BA.debugLineNum = 917;BA.debugLine="imgAbdomen.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 918;BA.debugLine="btnAbdomen1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 919;BA.debugLine="btnAbdomen2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 920;BA.debugLine="btnAbdomen3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 922;BA.debugLine="If spnCabeza.Visible = True Then";
if (mostCurrent._spncabeza.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 923;BA.debugLine="spnCabeza.SelectedIndex = 0";
mostCurrent._spncabeza.setSelectedIndex((int) (0));
 //BA.debugLineNum = 924;BA.debugLine="imgCabeza.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 925;BA.debugLine="btnCabeza1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 926;BA.debugLine="btnCabeza2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 927;BA.debugLine="btnCabeza3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 929;BA.debugLine="If spnTorax.Visible = True Then";
if (mostCurrent._spntorax.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 930;BA.debugLine="spnTorax.SelectedIndex = 0";
mostCurrent._spntorax.setSelectedIndex((int) (0));
 //BA.debugLineNum = 931;BA.debugLine="imgTorax.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 932;BA.debugLine="btnTorax1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 933;BA.debugLine="btnTorax2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 934;BA.debugLine="btnTorax3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 935;BA.debugLine="btnTorax4.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 937;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 938;BA.debugLine="End Sub";
return "";
}
public static String  _btnokseleccionmosquito_click() throws Exception{
String _eleccion = "";
 //BA.debugLineNum = 1123;BA.debugLine="Sub btnOkSeleccionMosquito_Click";
 //BA.debugLineNum = 1124;BA.debugLine="Dim eleccion As String";
_eleccion = "";
 //BA.debugLineNum = 1125;BA.debugLine="eleccion = spnEspecie.SelectedItem";
_eleccion = mostCurrent._spnespecie.getSelectedItem();
 //BA.debugLineNum = 1126;BA.debugLine="MsgBoxAsync(\"Lo más probable es que el mosquito";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Lo más probable es que el mosquito que encontraste sea un "+_eleccion),BA.ObjectToCharSequence("¿Qué especie es?"),processBA);
 //BA.debugLineNum = 1127;BA.debugLine="End Sub";
return "";
}
public static String  _btnpatas1_click() throws Exception{
 //BA.debugLineNum = 359;BA.debugLine="Sub btnPatas1_Click";
 //BA.debugLineNum = 360;BA.debugLine="imgPatas.Bitmap = LoadBitmap(File.DirAssets, \"pat";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas1.png").getObject()));
 //BA.debugLineNum = 361;BA.debugLine="spnPatas.SelectedIndex = 1";
mostCurrent._spnpatas.setSelectedIndex((int) (1));
 //BA.debugLineNum = 362;BA.debugLine="imgPatas.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 363;BA.debugLine="imgPatas.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 364;BA.debugLine="imgPatas.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 365;BA.debugLine="imgPatas.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 366;BA.debugLine="btnPatas1.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 367;BA.debugLine="btnPatas2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 369;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 370;BA.debugLine="End Sub";
return "";
}
public static String  _btnpatas2_click() throws Exception{
 //BA.debugLineNum = 371;BA.debugLine="Sub btnPatas2_Click";
 //BA.debugLineNum = 372;BA.debugLine="imgPatas.Bitmap = LoadBitmap(File.DirAssets, \"pat";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas2.png").getObject()));
 //BA.debugLineNum = 373;BA.debugLine="spnPatas.SelectedIndex = 2";
mostCurrent._spnpatas.setSelectedIndex((int) (2));
 //BA.debugLineNum = 374;BA.debugLine="imgPatas.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 375;BA.debugLine="imgPatas.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 376;BA.debugLine="imgPatas.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 377;BA.debugLine="imgPatas.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 378;BA.debugLine="btnPatas1.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 379;BA.debugLine="btnPatas2.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 381;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 382;BA.debugLine="End Sub";
return "";
}
public static String  _btntorax1_click() throws Exception{
 //BA.debugLineNum = 718;BA.debugLine="Sub btnTorax1_Click";
 //BA.debugLineNum = 719;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"Tor";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Torax1.png").getObject()));
 //BA.debugLineNum = 720;BA.debugLine="spnTorax.SelectedIndex = 1";
mostCurrent._spntorax.setSelectedIndex((int) (1));
 //BA.debugLineNum = 721;BA.debugLine="imgTorax.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 722;BA.debugLine="imgTorax.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 723;BA.debugLine="imgTorax.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 724;BA.debugLine="imgTorax.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 725;BA.debugLine="btnTorax1.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 726;BA.debugLine="btnTorax2.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 727;BA.debugLine="btnTorax3.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 728;BA.debugLine="btnTorax4.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 731;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 732;BA.debugLine="End Sub";
return "";
}
public static String  _btntorax2_click() throws Exception{
 //BA.debugLineNum = 733;BA.debugLine="Sub btnTorax2_Click";
 //BA.debugLineNum = 734;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"Tor";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Torax2.png").getObject()));
 //BA.debugLineNum = 735;BA.debugLine="spnTorax.SelectedIndex = 2";
mostCurrent._spntorax.setSelectedIndex((int) (2));
 //BA.debugLineNum = 736;BA.debugLine="imgTorax.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 737;BA.debugLine="imgTorax.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 738;BA.debugLine="imgTorax.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 739;BA.debugLine="imgTorax.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 740;BA.debugLine="btnTorax1.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 741;BA.debugLine="btnTorax2.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 742;BA.debugLine="btnTorax3.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 743;BA.debugLine="btnTorax4.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 745;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 746;BA.debugLine="End Sub";
return "";
}
public static String  _btntorax3_click() throws Exception{
 //BA.debugLineNum = 747;BA.debugLine="Sub btnTorax3_Click";
 //BA.debugLineNum = 748;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"Tor";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Torax3.png").getObject()));
 //BA.debugLineNum = 749;BA.debugLine="spnTorax.SelectedIndex = 3";
mostCurrent._spntorax.setSelectedIndex((int) (3));
 //BA.debugLineNum = 750;BA.debugLine="imgTorax.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 751;BA.debugLine="imgTorax.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 752;BA.debugLine="imgTorax.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 753;BA.debugLine="imgTorax.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 754;BA.debugLine="btnTorax1.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 755;BA.debugLine="btnTorax2.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 756;BA.debugLine="btnTorax3.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 757;BA.debugLine="btnTorax4.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 759;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 760;BA.debugLine="End Sub";
return "";
}
public static String  _btntorax4_click() throws Exception{
 //BA.debugLineNum = 761;BA.debugLine="Sub btnTorax4_Click";
 //BA.debugLineNum = 762;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"Tor";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Torax4.png").getObject()));
 //BA.debugLineNum = 763;BA.debugLine="spnTorax.SelectedIndex = 4";
mostCurrent._spntorax.setSelectedIndex((int) (4));
 //BA.debugLineNum = 764;BA.debugLine="imgTorax.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 765;BA.debugLine="imgTorax.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 766;BA.debugLine="imgTorax.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 767;BA.debugLine="imgTorax.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 768;BA.debugLine="btnTorax1.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 769;BA.debugLine="btnTorax2.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 770;BA.debugLine="btnTorax3.Enabled =True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 771;BA.debugLine="btnTorax4.Enabled =False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 773;BA.debugLine="CheckEspecie";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 774;BA.debugLine="End Sub";
return "";
}
public static String  _btnvermifoto_click() throws Exception{
 //BA.debugLineNum = 1162;BA.debugLine="Sub btnVerMiFoto_Click";
 //BA.debugLineNum = 1164;BA.debugLine="fondogris.Initialize(\"fondogris\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"fondogris");
 //BA.debugLineNum = 1165;BA.debugLine="fondogris.Color = Colors.ARGB(150,0,0,0)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1166;BA.debugLine="Activity.AddView(fondogris, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1169;BA.debugLine="scroll2d.Initialize(1000,1000,\"scroll2d\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,(int) (1000),(int) (1000),"scroll2d");
 //BA.debugLineNum = 1170;BA.debugLine="scroll2d.Color = Colors.Black";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1171;BA.debugLine="Activity.AddView(scroll2d, 10%x, 10%y, 80%x, 80%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 1173;BA.debugLine="bmp.InitializeResize(File.DirRootExternal & \"/Geo";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.InitializeResize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GeoVin/",_vvvvvvv0+".jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1174;BA.debugLine="imgUsuario.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1175;BA.debugLine="imgUsuario.SetBackgroundImage(bmp)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.SetBackgroundImageNew((android.graphics.Bitmap)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getObject()));
 //BA.debugLineNum = 1176;BA.debugLine="imgUsuario.Tag = \"1\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setTag((Object)("1"));
 //BA.debugLineNum = 1177;BA.debugLine="imgUsuario.Gravity = Gravity.FILL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 1179;BA.debugLine="scroll2d.panel.AddView(imgUsuario, (Activity.Widt";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getObject()),(int) ((mostCurrent._activity.getWidth()/(double)2-mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getWidth()/(double)2)),(int) (0),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getWidth(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getHeight());
 //BA.debugLineNum = 1180;BA.debugLine="scroll2d.Panel.Width = Activity.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 1181;BA.debugLine="scroll2d.Panel.Height = bmp.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().setHeight(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getHeight());
 //BA.debugLineNum = 1183;BA.debugLine="GD.SetOnGestureListener(scroll2d,\"pnl_gesture\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.SetOnGestureListener(processBA,(android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),"pnl_gesture");
 //BA.debugLineNum = 1184;BA.debugLine="GD.EnableLongPress(False)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.EnableLongPress(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1187;BA.debugLine="fotoCerrar.Initialize(\"fotoCerrar\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"fotoCerrar");
 //BA.debugLineNum = 1188;BA.debugLine="fotoCerrar.Text = \"X\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setText(BA.ObjectToCharSequence("X"));
 //BA.debugLineNum = 1189;BA.debugLine="fotoCerrar.Color = Colors.ARGB(100,255,255,255)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 1190;BA.debugLine="Activity.AddView(fotoCerrar, Activity.Width - 50d";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 1193;BA.debugLine="fotoCambiar.Initialize(\"fotoCambiar\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"fotoCambiar");
 //BA.debugLineNum = 1194;BA.debugLine="fotoCambiar.Text = \"Cambiar foto\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setText(BA.ObjectToCharSequence("Cambiar foto"));
 //BA.debugLineNum = 1195;BA.debugLine="fotoCambiar.Color = Colors.ARGB(100,255,255,255)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 1196;BA.debugLine="Activity.AddView(fotoCambiar, 10dip, 10dip, 40%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 1199;BA.debugLine="btnContinuar.Visible = False";
mostCurrent._btncontinuar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1200;BA.debugLine="btnVerMiFoto.Visible = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1202;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
 //BA.debugLineNum = 208;BA.debugLine="Sub CargarEspecies";
 //BA.debugLineNum = 212;BA.debugLine="imgCabeza.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 213;BA.debugLine="imgAbdomen.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 214;BA.debugLine="imgTorax.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 215;BA.debugLine="imgPatas.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 216;BA.debugLine="Activity.AddView(imgCabeza, 0, 0, 10dip, 10dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 217;BA.debugLine="Activity.AddView(imgAbdomen, 0, 0, 10dip, 10dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 218;BA.debugLine="Activity.AddView(imgTorax, 0, 0, 10dip, 10dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 219;BA.debugLine="Activity.AddView(imgPatas, 0, 0, 10dip, 10dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 220;BA.debugLine="imgCabeza.Gravity = Gravity.FILL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 221;BA.debugLine="imgAbdomen.Gravity = Gravity.FILL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 222;BA.debugLine="imgTorax.Gravity = Gravity.FILL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 223;BA.debugLine="imgPatas.Gravity = Gravity.FILL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 226;BA.debugLine="spnCabeza.Add(\"No lo sé\")";
mostCurrent._spncabeza.Add("No lo sé");
 //BA.debugLineNum = 227;BA.debugLine="spnCabeza.Add(\"Con marcas blancas y patrón de col";
mostCurrent._spncabeza.Add("Con marcas blancas y patrón de coloración");
 //BA.debugLineNum = 228;BA.debugLine="spnCabeza.Add(\"Con marcas blancas y una línea med";
mostCurrent._spncabeza.Add("Con marcas blancas y una línea media");
 //BA.debugLineNum = 229;BA.debugLine="spnCabeza.Add(\"Sin marcas blancas\")";
mostCurrent._spncabeza.Add("Sin marcas blancas");
 //BA.debugLineNum = 230;BA.debugLine="spnTorax.Add(\"No lo sé\")";
mostCurrent._spntorax.Add("No lo sé");
 //BA.debugLineNum = 231;BA.debugLine="spnTorax.Add(\"Con marcas blancas y dibujo en form";
mostCurrent._spntorax.Add("Con marcas blancas y dibujo en forma de lira");
 //BA.debugLineNum = 232;BA.debugLine="spnTorax.Add(\"Con marcas blancas y una línea medi";
mostCurrent._spntorax.Add("Con marcas blancas y una línea media dorsal");
 //BA.debugLineNum = 233;BA.debugLine="spnTorax.Add(\"Sin marcas blancas y un patrón de c";
mostCurrent._spntorax.Add("Sin marcas blancas y un patrón de coloración");
 //BA.debugLineNum = 234;BA.debugLine="spnTorax.Add(\"Sin marcas blancas y color uniforme";
mostCurrent._spntorax.Add("Sin marcas blancas y color uniforme");
 //BA.debugLineNum = 235;BA.debugLine="spnAbdomen.Add(\"No lo sé\")";
mostCurrent._spnabdomen.Add("No lo sé");
 //BA.debugLineNum = 236;BA.debugLine="spnAbdomen.Add(\"Con extremo distal en punta y ban";
mostCurrent._spnabdomen.Add("Con extremo distal en punta y bandas blancas");
 //BA.debugLineNum = 237;BA.debugLine="spnAbdomen.Add(\"Con extremo distal en punta y una";
mostCurrent._spnabdomen.Add("Con extremo distal en punta y una línea media");
 //BA.debugLineNum = 238;BA.debugLine="spnAbdomen.Add(\"Con extremo distal redondeado\")";
mostCurrent._spnabdomen.Add("Con extremo distal redondeado");
 //BA.debugLineNum = 239;BA.debugLine="spnPatas.Add(\"No lo sé\")";
mostCurrent._spnpatas.Add("No lo sé");
 //BA.debugLineNum = 240;BA.debugLine="spnPatas.Add(\"Con bandas blancas\")";
mostCurrent._spnpatas.Add("Con bandas blancas");
 //BA.debugLineNum = 241;BA.debugLine="spnPatas.Add(\"Sin bandas blancas\")";
mostCurrent._spnpatas.Add("Sin bandas blancas");
 //BA.debugLineNum = 242;BA.debugLine="spnEspecie.Add(\"Aedes aegypti\")";
mostCurrent._spnespecie.Add("Aedes aegypti");
 //BA.debugLineNum = 243;BA.debugLine="spnEspecie.Add(\"Aedes albifasciatus\")";
mostCurrent._spnespecie.Add("Aedes albifasciatus");
 //BA.debugLineNum = 244;BA.debugLine="spnEspecie.Add(\"Aedes albopictus\")";
mostCurrent._spnespecie.Add("Aedes albopictus");
 //BA.debugLineNum = 245;BA.debugLine="spnEspecie.Add(\"Culex sp.\")";
mostCurrent._spnespecie.Add("Culex sp.");
 //BA.debugLineNum = 246;BA.debugLine="spnEspecie.Add(\"Indeterminado\")";
mostCurrent._spnespecie.Add("Indeterminado");
 //BA.debugLineNum = 247;BA.debugLine="spnEspecie.DropdownBackgroundColor = Colors.Light";
mostCurrent._spnespecie.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 248;BA.debugLine="spnEspecie.DropdownTextColor = Colors.Black";
mostCurrent._spnespecie.setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 251;BA.debugLine="spnEspecie.TextColor = Colors.DarkGray";
mostCurrent._spnespecie.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 252;BA.debugLine="lblCabeza_Click";
_lblcabeza_click();
 //BA.debugLineNum = 254;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
 //BA.debugLineNum = 941;BA.debugLine="Sub CargarPartesMosquito";
 //BA.debugLineNum = 943;BA.debugLine="If spnCabeza.SelectedIndex = 1 Then";
if (mostCurrent._spncabeza.getSelectedIndex()==1) { 
 //BA.debugLineNum = 944;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"c";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza1.png").getObject()));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==2) { 
 //BA.debugLineNum = 946;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"c";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza2.png").getObject()));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==3) { 
 //BA.debugLineNum = 948;BA.debugLine="imgCabeza.Bitmap = LoadBitmap(File.DirAssets, \"c";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza3.png").getObject()));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==0) { 
 //BA.debugLineNum = 950;BA.debugLine="imgCabeza.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 953;BA.debugLine="If spnTorax.SelectedIndex = 1 Then";
if (mostCurrent._spntorax.getSelectedIndex()==1) { 
 //BA.debugLineNum = 954;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"to";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax1.png").getObject()));
 }else if(mostCurrent._spntorax.getSelectedIndex()==2) { 
 //BA.debugLineNum = 956;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"to";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax2.png").getObject()));
 }else if(mostCurrent._spntorax.getSelectedIndex()==3) { 
 //BA.debugLineNum = 958;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"to";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax3.png").getObject()));
 }else if(mostCurrent._spntorax.getSelectedIndex()==4) { 
 //BA.debugLineNum = 960;BA.debugLine="imgTorax.Bitmap = LoadBitmap(File.DirAssets, \"to";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax4.png").getObject()));
 }else if(mostCurrent._spntorax.getSelectedIndex()==0) { 
 //BA.debugLineNum = 962;BA.debugLine="imgTorax.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 965;BA.debugLine="If spnAbdomen.SelectedIndex= 1 Then";
if (mostCurrent._spnabdomen.getSelectedIndex()==1) { 
 //BA.debugLineNum = 966;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen1.png").getObject()));
 }else if(mostCurrent._spnabdomen.getSelectedIndex()==2) { 
 //BA.debugLineNum = 968;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen2.png").getObject()));
 }else if(mostCurrent._spnabdomen.getSelectedIndex()==3) { 
 //BA.debugLineNum = 970;BA.debugLine="imgAbdomen.Bitmap = LoadBitmap(File.DirAssets, \"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen3.png").getObject()));
 }else if(mostCurrent._spnabdomen.getSelectedIndex()==0) { 
 //BA.debugLineNum = 972;BA.debugLine="imgAbdomen.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 976;BA.debugLine="If spnPatas.SelectedIndex = 1 Then";
if (mostCurrent._spnpatas.getSelectedIndex()==1) { 
 //BA.debugLineNum = 977;BA.debugLine="imgPatas.Bitmap = LoadBitmap(File.DirAssets, \"pa";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas1.png").getObject()));
 }else if(mostCurrent._spnpatas.getSelectedIndex()==2) { 
 //BA.debugLineNum = 979;BA.debugLine="imgPatas.Bitmap = LoadBitmap(File.DirAssets, \"pa";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas2.png").getObject()));
 }else if(mostCurrent._spnpatas.getSelectedIndex()==0) { 
 //BA.debugLineNum = 981;BA.debugLine="imgPatas.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 };
 //BA.debugLineNum = 985;BA.debugLine="imgCabeza.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 986;BA.debugLine="imgCabeza.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 987;BA.debugLine="imgCabeza.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 988;BA.debugLine="imgCabeza.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 989;BA.debugLine="imgTorax.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 990;BA.debugLine="imgTorax.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 991;BA.debugLine="imgTorax.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 992;BA.debugLine="imgTorax.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 993;BA.debugLine="imgAbdomen.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 994;BA.debugLine="imgAbdomen.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 995;BA.debugLine="imgAbdomen.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 996;BA.debugLine="imgAbdomen.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 997;BA.debugLine="imgPatas.Left = imgMosquito.Left";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 998;BA.debugLine="imgPatas.Top = imgMosquito.Top";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 999;BA.debugLine="imgPatas.Height = imgMosquito.Height";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 1000;BA.debugLine="imgPatas.Width = imgMosquito.Width";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 1001;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0() throws Exception{
int _pgb1 = 0;
int _pgb2 = 0;
int _pgb3 = 0;
int _pgb4 = 0;
 //BA.debugLineNum = 1033;BA.debugLine="Sub CheckEspecie";
 //BA.debugLineNum = 1034;BA.debugLine="pgbAegipty.Progress = 0";
mostCurrent._pgbaegipty.setProgress((int) (0));
 //BA.debugLineNum = 1035;BA.debugLine="pgbAlbifasciatus.Progress = 0";
mostCurrent._pgbalbifasciatus.setProgress((int) (0));
 //BA.debugLineNum = 1036;BA.debugLine="pgbAlbopictus.Progress = 0";
mostCurrent._pgbalbopictus.setProgress((int) (0));
 //BA.debugLineNum = 1037;BA.debugLine="pgbCulex.Progress = 0";
mostCurrent._pgbculex.setProgress((int) (0));
 //BA.debugLineNum = 1040;BA.debugLine="If spnCabeza.SelectedIndex = 1 Then";
if (mostCurrent._spncabeza.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1041;BA.debugLine="pgbAegipty.Progress = pgbAegipty.Progress + 5";
mostCurrent._pgbaegipty.setProgress((int) (mostCurrent._pgbaegipty.getProgress()+5));
 };
 //BA.debugLineNum = 1043;BA.debugLine="If spnCabeza.SelectedIndex = 2 Then";
if (mostCurrent._spncabeza.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1044;BA.debugLine="pgbAlbifasciatus.Progress = pgbAlbifasciatus.Pro";
mostCurrent._pgbalbifasciatus.setProgress((int) (mostCurrent._pgbalbifasciatus.getProgress()+5));
 };
 //BA.debugLineNum = 1046;BA.debugLine="If spnCabeza.SelectedIndex = 3 Then";
if (mostCurrent._spncabeza.getSelectedIndex()==3) { 
 //BA.debugLineNum = 1047;BA.debugLine="pgbAlbopictus.Progress = pgbAlbopictus.Progress";
mostCurrent._pgbalbopictus.setProgress((int) (mostCurrent._pgbalbopictus.getProgress()+5));
 //BA.debugLineNum = 1048;BA.debugLine="pgbCulex.Progress = pgbCulex.Progress + 5";
mostCurrent._pgbculex.setProgress((int) (mostCurrent._pgbculex.getProgress()+5));
 };
 //BA.debugLineNum = 1052;BA.debugLine="If spnAbdomen.SelectedIndex = 1 Then";
if (mostCurrent._spnabdomen.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1053;BA.debugLine="pgbAegipty.Progress = pgbAegipty.Progress + 15";
mostCurrent._pgbaegipty.setProgress((int) (mostCurrent._pgbaegipty.getProgress()+15));
 //BA.debugLineNum = 1054;BA.debugLine="pgbAlbifasciatus.Progress = pgbAlbifasciatus.Pro";
mostCurrent._pgbalbifasciatus.setProgress((int) (mostCurrent._pgbalbifasciatus.getProgress()+15));
 };
 //BA.debugLineNum = 1056;BA.debugLine="If spnAbdomen.SelectedIndex = 2 Then";
if (mostCurrent._spnabdomen.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1057;BA.debugLine="pgbAlbopictus.Progress = pgbAlbopictus.Progress";
mostCurrent._pgbalbopictus.setProgress((int) (mostCurrent._pgbalbopictus.getProgress()+15));
 };
 //BA.debugLineNum = 1059;BA.debugLine="If spnAbdomen.SelectedIndex = 3 Then";
if (mostCurrent._spnabdomen.getSelectedIndex()==3) { 
 //BA.debugLineNum = 1060;BA.debugLine="pgbCulex.Progress = pgbCulex.Progress + 15";
mostCurrent._pgbculex.setProgress((int) (mostCurrent._pgbculex.getProgress()+15));
 };
 //BA.debugLineNum = 1064;BA.debugLine="If spnTorax.SelectedIndex = 1 Then";
if (mostCurrent._spntorax.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1065;BA.debugLine="pgbAegipty.Progress = pgbAegipty.Progress + 40";
mostCurrent._pgbaegipty.setProgress((int) (mostCurrent._pgbaegipty.getProgress()+40));
 };
 //BA.debugLineNum = 1067;BA.debugLine="If spnTorax.SelectedIndex = 2 Then";
if (mostCurrent._spntorax.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1068;BA.debugLine="pgbAlbifasciatus.Progress = pgbAlbifasciatus.Pro";
mostCurrent._pgbalbifasciatus.setProgress((int) (mostCurrent._pgbalbifasciatus.getProgress()+40));
 };
 //BA.debugLineNum = 1070;BA.debugLine="If spnTorax.SelectedIndex = 3 Then";
if (mostCurrent._spntorax.getSelectedIndex()==3) { 
 //BA.debugLineNum = 1071;BA.debugLine="pgbAlbopictus.Progress = pgbAlbopictus.Progress";
mostCurrent._pgbalbopictus.setProgress((int) (mostCurrent._pgbalbopictus.getProgress()+40));
 };
 //BA.debugLineNum = 1073;BA.debugLine="If spnTorax.SelectedIndex = 4 Then";
if (mostCurrent._spntorax.getSelectedIndex()==4) { 
 //BA.debugLineNum = 1074;BA.debugLine="pgbCulex.Progress = pgbCulex.Progress + 40";
mostCurrent._pgbculex.setProgress((int) (mostCurrent._pgbculex.getProgress()+40));
 };
 //BA.debugLineNum = 1077;BA.debugLine="If spnPatas.SelectedIndex = 1 Then";
if (mostCurrent._spnpatas.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1078;BA.debugLine="pgbAegipty.Progress = pgbAegipty.Progress + 40";
mostCurrent._pgbaegipty.setProgress((int) (mostCurrent._pgbaegipty.getProgress()+40));
 //BA.debugLineNum = 1079;BA.debugLine="pgbAlbifasciatus.Progress = pgbAlbifasciatus.Pro";
mostCurrent._pgbalbifasciatus.setProgress((int) (mostCurrent._pgbalbifasciatus.getProgress()+40));
 };
 //BA.debugLineNum = 1081;BA.debugLine="If spnPatas.SelectedIndex = 2 Then";
if (mostCurrent._spnpatas.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1082;BA.debugLine="pgbAlbopictus.Progress = pgbAlbopictus.Progress";
mostCurrent._pgbalbopictus.setProgress((int) (mostCurrent._pgbalbopictus.getProgress()+40));
 //BA.debugLineNum = 1083;BA.debugLine="pgbCulex.Progress = pgbCulex.Progress + 40";
mostCurrent._pgbculex.setProgress((int) (mostCurrent._pgbculex.getProgress()+40));
 };
 //BA.debugLineNum = 1087;BA.debugLine="If spnCabeza.SelectedIndex = 1 And spnTorax.Selec";
if (mostCurrent._spncabeza.getSelectedIndex()==1 && mostCurrent._spntorax.getSelectedIndex()==1 && mostCurrent._spnabdomen.getSelectedIndex()==1 && mostCurrent._spnpatas.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1088;BA.debugLine="spnEspecie.SelectedIndex = 0";
mostCurrent._spnespecie.setSelectedIndex((int) (0));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==2 && mostCurrent._spntorax.getSelectedIndex()==2 && mostCurrent._spnabdomen.getSelectedIndex()==1 && mostCurrent._spnpatas.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1090;BA.debugLine="spnEspecie.SelectedIndex = 2";
mostCurrent._spnespecie.setSelectedIndex((int) (2));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==3 && mostCurrent._spntorax.getSelectedIndex()==3 && mostCurrent._spnabdomen.getSelectedIndex()==2 && mostCurrent._spnpatas.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1092;BA.debugLine="spnEspecie.SelectedIndex = 1";
mostCurrent._spnespecie.setSelectedIndex((int) (1));
 }else if(mostCurrent._spncabeza.getSelectedIndex()==3 && mostCurrent._spntorax.getSelectedIndex()==4 && mostCurrent._spnabdomen.getSelectedIndex()==3 && mostCurrent._spnpatas.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1094;BA.debugLine="spnEspecie.SelectedIndex = 3";
mostCurrent._spnespecie.setSelectedIndex((int) (3));
 }else {
 //BA.debugLineNum = 1097;BA.debugLine="Dim pgb1 As Int = pgbAegipty.Progress";
_pgb1 = mostCurrent._pgbaegipty.getProgress();
 //BA.debugLineNum = 1098;BA.debugLine="Dim pgb2 As Int = pgbAlbifasciatus.Progress";
_pgb2 = mostCurrent._pgbalbifasciatus.getProgress();
 //BA.debugLineNum = 1099;BA.debugLine="Dim pgb3 As Int = pgbAlbopictus.Progress";
_pgb3 = mostCurrent._pgbalbopictus.getProgress();
 //BA.debugLineNum = 1100;BA.debugLine="Dim pgb4 As Int = pgbCulex.Progress";
_pgb4 = mostCurrent._pgbculex.getProgress();
 //BA.debugLineNum = 1102;BA.debugLine="If pgb1 > pgb2 And pgb1 > pgb3 And pgb1 > pgb4 T";
if (_pgb1>_pgb2 && _pgb1>_pgb3 && _pgb1>_pgb4) { 
 //BA.debugLineNum = 1103;BA.debugLine="spnEspecie.SelectedIndex = 0";
mostCurrent._spnespecie.setSelectedIndex((int) (0));
 //BA.debugLineNum = 1104;BA.debugLine="lblEspecie.Text = \"Especie más probable (\" & pg";
mostCurrent._lblespecie.setText(BA.ObjectToCharSequence("Especie más probable ("+BA.NumberToString(_pgb1)+"%)"));
 }else if(_pgb2>_pgb3 && _pgb2>_pgb4 && _pgb2>_pgb1) { 
 //BA.debugLineNum = 1106;BA.debugLine="spnEspecie.SelectedIndex = 2";
mostCurrent._spnespecie.setSelectedIndex((int) (2));
 //BA.debugLineNum = 1107;BA.debugLine="lblEspecie.Text = \"Especie más probable (\" & pg";
mostCurrent._lblespecie.setText(BA.ObjectToCharSequence("Especie más probable ("+BA.NumberToString(_pgb2)+"%)"));
 }else if(_pgb3>_pgb1 && _pgb3>_pgb2 && _pgb3>_pgb4) { 
 //BA.debugLineNum = 1109;BA.debugLine="spnEspecie.SelectedIndex = 1";
mostCurrent._spnespecie.setSelectedIndex((int) (1));
 //BA.debugLineNum = 1110;BA.debugLine="lblEspecie.Text = \"Especie más probable (\" & pg";
mostCurrent._lblespecie.setText(BA.ObjectToCharSequence("Especie más probable ("+BA.NumberToString(_pgb3)+"%)"));
 }else if(_pgb4>_pgb1 && _pgb4>_pgb2 && _pgb4>_pgb3) { 
 //BA.debugLineNum = 1112;BA.debugLine="spnEspecie.SelectedIndex = 3";
mostCurrent._spnespecie.setSelectedIndex((int) (3));
 //BA.debugLineNum = 1113;BA.debugLine="lblEspecie.Text = \"Especie más probable (\" & pg";
mostCurrent._lblespecie.setText(BA.ObjectToCharSequence("Especie más probable ("+BA.NumberToString(_pgb4)+"%)"));
 }else {
 //BA.debugLineNum = 1116;BA.debugLine="spnEspecie.SelectedIndex = 4";
mostCurrent._spnespecie.setSelectedIndex((int) (4));
 };
 };
 //BA.debugLineNum = 1121;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 31;BA.debugLine="Dim scroll2d As ScrollView2D";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private scrollOptions As HorizontalScrollView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim pregunta1 As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim pregunta2 As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim imagenUsuario1 As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim imagenUsuario2 As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 40;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btnContinuar As Button";
mostCurrent._btncontinuar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim opcionElegida As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = 0;
 //BA.debugLineNum = 48;BA.debugLine="Private lblInstrucciones As Label";
mostCurrent._lblinstrucciones = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lblNombre As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private imgVinchuca11 As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Dim Container As AHPageContainer";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new de.amberhome.viewpager.AHPageContainer();
 //BA.debugLineNum = 54;BA.debugLine="Dim Pager As AHViewPager";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new de.amberhome.viewpager.AHViewPager();
 //BA.debugLineNum = 55;BA.debugLine="Private lblFondo As Label";
mostCurrent._lblfondo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private btnVolver As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private imgHabitat As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private imgDistribucion As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private lblDescripcionGeneral As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Dim GD As GestureDetector";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new flm.b4a.gesturedetector.GestureDetectorForB4A();
 //BA.debugLineNum = 63;BA.debugLine="Dim zoom As Float = 1.0";
_vvvvvvvvvvvvvvvvvvvvvvvvvvv0 = (float) (1.0);
 //BA.debugLineNum = 64;BA.debugLine="Dim imgUsuario As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private bmp As Bitmap";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private btnCambiarFoto As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private lblSlide As Label";
mostCurrent._lblslide = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private btnEnviar As Button";
mostCurrent._btnenviar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private lblRojo As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private imgOpcion1_1 As ImageView";
mostCurrent._imgopcion1_1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private imgOpcion2_1 As ImageView";
mostCurrent._imgopcion2_1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private imgOpcion4_1 As ImageView";
mostCurrent._imgopcion4_1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private imgOpcion3_1 As ImageView";
mostCurrent._imgopcion3_1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Dim fondogris As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Dim fotoCerrar As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Dim fotoCambiar As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private btnVerMiFoto As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private textOpcion1 As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private textOpcion2 As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private textOpcion3 As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private textOpcion4 As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Dim currentscreen As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 93;BA.debugLine="Private chkEncuesta As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private chkFotos As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private chkNotas As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private txtNotas As EditText";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private imgSitio1 As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private imgSitio2 As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private imgSitio3 As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private imgSitio4 As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private spinPregunta1 As Spinner";
mostCurrent._spinpregunta1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private lblRespuesta As Label";
mostCurrent._lblrespuesta = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private spinPregunta2 As Spinner";
mostCurrent._spinpregunta2 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private spinPregunta3 As Spinner";
mostCurrent._spinpregunta3 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Private lblPregunta2 As Label";
mostCurrent._lblpregunta2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private lblPregunta3 As Label";
mostCurrent._lblpregunta3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 110;BA.debugLine="Private chkLocalizacion As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private btnOk As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 112;BA.debugLine="Private btnOkSeleccionMosquito As Button";
mostCurrent._btnokseleccionmosquito = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 116;BA.debugLine="Dim tutorialstep As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = "";
 //BA.debugLineNum = 117;BA.debugLine="Dim cdtutorial As ColorDrawable";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 118;BA.debugLine="Dim lblHighlightTutorial As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private spnCabeza As Spinner";
mostCurrent._spncabeza = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private spnTorax As Spinner";
mostCurrent._spntorax = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private spnAbdomen As Spinner";
mostCurrent._spnabdomen = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private spnPatas As Spinner";
mostCurrent._spnpatas = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private spnEspecie As Spinner";
mostCurrent._spnespecie = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 127;BA.debugLine="Private imgTorax As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private imgCabeza As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private imgAbdomen As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private imgPatas As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private imgMosquito As ImageView";
mostCurrent._imgmosquito = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Dim btnAbdomen1 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Dim btnAbdomen2 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 135;BA.debugLine="Dim btnAbdomen3 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Dim btnCabeza1 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Dim btnCabeza2 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 138;BA.debugLine="Dim btnCabeza3 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Dim btnTorax1 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 140;BA.debugLine="Dim btnTorax2 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Dim btnTorax3 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 142;BA.debugLine="Dim btnTorax4 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 143;BA.debugLine="Dim btnPatas1 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Dim btnPatas2 As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Private scrPartes As ScrollView";
mostCurrent._scrpartes = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Private lblCabeza As Label";
mostCurrent._lblcabeza = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 149;BA.debugLine="Private lblPatas As Label";
mostCurrent._lblpatas = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 150;BA.debugLine="Private lblAbdomen As Label";
mostCurrent._lblabdomen = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 151;BA.debugLine="Private lblTorax As Label";
mostCurrent._lbltorax = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 153;BA.debugLine="Dim p As Phone";
_vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 154;BA.debugLine="Private btnNoSe As Button";
mostCurrent._btnnose = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 156;BA.debugLine="Private lblEspecie As Label";
mostCurrent._lblespecie = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 157;BA.debugLine="Private lblFotos As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 159;BA.debugLine="Private pgbCulex As ProgressBar";
mostCurrent._pgbculex = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 160;BA.debugLine="Private pgbAlbifasciatus As ProgressBar";
mostCurrent._pgbalbifasciatus = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 161;BA.debugLine="Private pgbAlbopictus As ProgressBar";
mostCurrent._pgbalbopictus = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 162;BA.debugLine="Private pgbAegipty As ProgressBar";
mostCurrent._pgbaegipty = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 163;BA.debugLine="Private lblHighlight As Label";
mostCurrent._lblhighlight = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _lblabdomen_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgabdomen1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgabdomen2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgabdomen3 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfotoabdomen1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfotoabdomen2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfotoabdomen3 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _disabled = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _enabled = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld1 = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblabdomen1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblabdomen2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblabdomen3 = null;
int _i = 0;
 //BA.debugLineNum = 384;BA.debugLine="Sub lblAbdomen_Click";
 //BA.debugLineNum = 385;BA.debugLine="lblPatas.Color = Colors.Transparent";
mostCurrent._lblpatas.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 386;BA.debugLine="lblPatas.TextColor = Colors.white";
mostCurrent._lblpatas.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 387;BA.debugLine="lblAbdomen.Color = Colors.White";
mostCurrent._lblabdomen.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 388;BA.debugLine="lblAbdomen.TextColor = Colors.DarkGray";
mostCurrent._lblabdomen.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 389;BA.debugLine="lblCabeza.Color = Colors.Transparent";
mostCurrent._lblcabeza.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 390;BA.debugLine="lblCabeza.TextColor = Colors.white";
mostCurrent._lblcabeza.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 391;BA.debugLine="lblTorax.Color = Colors.Transparent";
mostCurrent._lbltorax.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 392;BA.debugLine="lblTorax.TextColor = Colors.white";
mostCurrent._lbltorax.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 395;BA.debugLine="Dim imgAbdomen1 As ImageView";
_imgabdomen1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 396;BA.debugLine="Dim imgAbdomen2 As ImageView";
_imgabdomen2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 397;BA.debugLine="Dim imgAbdomen3 As ImageView";
_imgabdomen3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 398;BA.debugLine="imgAbdomen1.Initialize(\"\")";
_imgabdomen1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 399;BA.debugLine="imgAbdomen2.Initialize(\"\")";
_imgabdomen2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 400;BA.debugLine="imgAbdomen3.Initialize(\"\")";
_imgabdomen3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 401;BA.debugLine="imgAbdomen1.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgabdomen1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen1-zoom.png").getObject()));
 //BA.debugLineNum = 402;BA.debugLine="imgAbdomen2.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgabdomen2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen2-zoom.png").getObject()));
 //BA.debugLineNum = 403;BA.debugLine="imgAbdomen3.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgabdomen3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen3-zoom.png").getObject()));
 //BA.debugLineNum = 404;BA.debugLine="imgAbdomen1.Gravity = Gravity.FILL";
_imgabdomen1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 405;BA.debugLine="imgAbdomen2.Gravity = Gravity.FILL";
_imgabdomen2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 406;BA.debugLine="imgAbdomen3.Gravity = Gravity.FILL";
_imgabdomen3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 410;BA.debugLine="Dim imgFotoAbdomen1 As ImageView";
_imgfotoabdomen1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 411;BA.debugLine="Dim imgFotoAbdomen2 As ImageView";
_imgfotoabdomen2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 412;BA.debugLine="Dim imgFotoAbdomen3 As ImageView";
_imgfotoabdomen3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 413;BA.debugLine="imgFotoAbdomen1.Initialize(\"\")";
_imgfotoabdomen1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 414;BA.debugLine="imgFotoAbdomen2.Initialize(\"\")";
_imgfotoabdomen2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 415;BA.debugLine="imgFotoAbdomen3.Initialize(\"\")";
_imgfotoabdomen3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 416;BA.debugLine="imgFotoAbdomen1.Bitmap = LoadBitmap(File.DirAsset";
_imgfotoabdomen1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen1-foto.png").getObject()));
 //BA.debugLineNum = 417;BA.debugLine="imgFotoAbdomen2.Bitmap = LoadBitmap(File.DirAsset";
_imgfotoabdomen2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen2-foto.png").getObject()));
 //BA.debugLineNum = 418;BA.debugLine="imgFotoAbdomen3.Bitmap = LoadBitmap(File.DirAsset";
_imgfotoabdomen3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"abdomen3-foto.png").getObject()));
 //BA.debugLineNum = 419;BA.debugLine="imgFotoAbdomen1.Gravity = Gravity.Fill";
_imgfotoabdomen1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 420;BA.debugLine="imgFotoAbdomen2.Gravity = Gravity.Fill";
_imgfotoabdomen2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 421;BA.debugLine="imgFotoAbdomen3.Gravity = Gravity.Fill";
_imgfotoabdomen3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 425;BA.debugLine="btnAbdomen1.Initialize(\"btnAbdomen1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"btnAbdomen1");
 //BA.debugLineNum = 426;BA.debugLine="btnAbdomen2.Initialize(\"btnAbdomen2\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(mostCurrent.activityBA,"btnAbdomen2");
 //BA.debugLineNum = 427;BA.debugLine="btnAbdomen3.Initialize(\"btnAbdomen3\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.Initialize(mostCurrent.activityBA,"btnAbdomen3");
 //BA.debugLineNum = 429;BA.debugLine="Dim disabled, enabled As ColorDrawable";
_disabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
_enabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 430;BA.debugLine="disabled.Initialize(Colors.ARGB(100,53,132,210),";
_disabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (53),(int) (132),(int) (210)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 431;BA.debugLine="enabled.Initialize(Colors.Transparent, 10dip)";
_enabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 432;BA.debugLine="Dim sld, sld1, sld2 As StateListDrawable";
_sld = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld1 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld2 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
 //BA.debugLineNum = 433;BA.debugLine="sld.Initialize";
_sld.Initialize();
 //BA.debugLineNum = 434;BA.debugLine="sld1.Initialize";
_sld1.Initialize();
 //BA.debugLineNum = 435;BA.debugLine="sld2.Initialize";
_sld2.Initialize();
 //BA.debugLineNum = 436;BA.debugLine="sld.AddState(sld.State_Disabled, disabled)";
_sld.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 437;BA.debugLine="sld.AddState(sld.State_Enabled, enabled)";
_sld.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 438;BA.debugLine="sld1.AddState(sld.State_Disabled, disabled)";
_sld1.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 439;BA.debugLine="sld1.AddState(sld.State_Enabled, enabled)";
_sld1.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 440;BA.debugLine="sld2.AddState(sld.State_Disabled, disabled)";
_sld2.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 441;BA.debugLine="sld2.AddState(sld.State_Enabled, enabled)";
_sld2.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 442;BA.debugLine="btnAbdomen1.Background = sld";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setBackground((android.graphics.drawable.Drawable)(_sld.getObject()));
 //BA.debugLineNum = 443;BA.debugLine="btnAbdomen2.Background = sld1";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setBackground((android.graphics.drawable.Drawable)(_sld1.getObject()));
 //BA.debugLineNum = 444;BA.debugLine="btnAbdomen3.Background = sld2";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setBackground((android.graphics.drawable.Drawable)(_sld2.getObject()));
 //BA.debugLineNum = 446;BA.debugLine="btnNoSe.Text = \"No se que como es el abdomen\"";
mostCurrent._btnnose.setText(BA.ObjectToCharSequence("No se que como es el abdomen"));
 //BA.debugLineNum = 449;BA.debugLine="Dim lblAbdomen1 As Label";
_lblabdomen1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 450;BA.debugLine="Dim lblAbdomen2 As Label";
_lblabdomen2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 451;BA.debugLine="Dim lblAbdomen3 As Label";
_lblabdomen3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 452;BA.debugLine="lblAbdomen1.Initialize(\"\")";
_lblabdomen1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 453;BA.debugLine="lblAbdomen2.Initialize(\"\")";
_lblabdomen2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 454;BA.debugLine="lblAbdomen3.Initialize(\"\")";
_lblabdomen3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 455;BA.debugLine="lblAbdomen1.Text = \"Terminación en punta y bandas";
_lblabdomen1.setText(BA.ObjectToCharSequence("Terminación en punta y bandas blancas"));
 //BA.debugLineNum = 456;BA.debugLine="lblAbdomen2.Text = \"Terminación en punta y una lí";
_lblabdomen2.setText(BA.ObjectToCharSequence("Terminación en punta y una línea media"));
 //BA.debugLineNum = 457;BA.debugLine="lblAbdomen3.Text = \"Terminación redondeada\"";
_lblabdomen3.setText(BA.ObjectToCharSequence("Terminación redondeada"));
 //BA.debugLineNum = 458;BA.debugLine="lblAbdomen1.Color = Colors.ARGB(150,255,255,255)";
_lblabdomen1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 459;BA.debugLine="lblAbdomen2.Color = Colors.ARGB(150,255,255,255)";
_lblabdomen2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 460;BA.debugLine="lblAbdomen3.Color = Colors.ARGB(150,255,255,255)";
_lblabdomen3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 461;BA.debugLine="lblAbdomen1.TextColor = Colors.Black";
_lblabdomen1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 462;BA.debugLine="lblAbdomen2.TextColor = Colors.Black";
_lblabdomen2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 463;BA.debugLine="lblAbdomen3.TextColor = Colors.Black";
_lblabdomen3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 464;BA.debugLine="lblAbdomen1.Gravity = Gravity.CENTER_HORIZONTAL";
_lblabdomen1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 465;BA.debugLine="lblAbdomen2.Gravity = Gravity.CENTER_HORIZONTAL";
_lblabdomen2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 466;BA.debugLine="lblAbdomen3.Gravity = Gravity.CENTER_HORIZONTAL";
_lblabdomen3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 469;BA.debugLine="For i = scrPartes.Panel.NumberOfViews - 1 To 0 St";
{
final int step71 = -1;
final int limit71 = (int) (0);
_i = (int) (mostCurrent._scrpartes.getPanel().getNumberOfViews()-1) ;
for (;_i >= limit71 ;_i = _i + step71 ) {
 //BA.debugLineNum = 470;BA.debugLine="scrPartes.Panel.RemoveViewAt(i)";
mostCurrent._scrpartes.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 472;BA.debugLine="scrPartes.Panel.AddView(imgAbdomen1, 10dip, 10dip";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgabdomen1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (mostCurrent._scrpartes.getWidth()/(double)8),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 473;BA.debugLine="scrPartes.Panel.AddView(imgFotoAbdomen1, imgAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfotoabdomen1.getObject()),(int) (_imgabdomen1.getLeft()+_imgabdomen1.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgabdomen1.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgabdomen1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgabdomen1.getHeight());
 //BA.debugLineNum = 474;BA.debugLine="scrPartes.Panel.AddView(lblAbdomen1, imgFotoAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblabdomen1.getObject()),_imgfotoabdomen1.getLeft(),_imgfotoabdomen1.getTop(),_imgfotoabdomen1.getWidth(),(int) (_imgfotoabdomen1.getHeight()/(double)4));
 //BA.debugLineNum = 475;BA.debugLine="scrPartes.Panel.AddView(btnAbdomen1, 0, imgAbdome";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),_imgabdomen1.getTop(),mostCurrent._scrpartes.getWidth(),_imgabdomen1.getHeight());
 //BA.debugLineNum = 477;BA.debugLine="scrPartes.Panel.AddView(imgAbdomen2, 10dip, imgAb";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgabdomen2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgabdomen1.getHeight()+_imgabdomen1.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)8),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 478;BA.debugLine="scrPartes.Panel.AddView(imgFotoAbdomen2, imgAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfotoabdomen2.getObject()),(int) (_imgabdomen2.getLeft()+_imgabdomen2.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgabdomen2.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgabdomen2.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgabdomen2.getHeight());
 //BA.debugLineNum = 479;BA.debugLine="scrPartes.Panel.AddView(lblAbdomen2, imgFotoAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblabdomen2.getObject()),_imgfotoabdomen2.getLeft(),_imgfotoabdomen2.getTop(),_imgfotoabdomen2.getWidth(),(int) (_imgfotoabdomen2.getHeight()/(double)4));
 //BA.debugLineNum = 480;BA.debugLine="scrPartes.Panel.AddView(btnAbdomen2, 0, imgAbdome";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getObject()),(int) (0),_imgabdomen2.getTop(),mostCurrent._scrpartes.getWidth(),_imgabdomen2.getHeight());
 //BA.debugLineNum = 482;BA.debugLine="scrPartes.Panel.AddView(imgAbdomen3, 10dip, imgAb";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgabdomen3.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgabdomen2.getHeight()+_imgabdomen2.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)8),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 483;BA.debugLine="scrPartes.Panel.AddView(imgFotoAbdomen3, imgAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfotoabdomen3.getObject()),(int) (_imgabdomen3.getLeft()+_imgabdomen3.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgabdomen3.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgabdomen3.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgabdomen3.getHeight());
 //BA.debugLineNum = 484;BA.debugLine="scrPartes.Panel.AddView(lblAbdomen3, imgFotoAbdom";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblabdomen3.getObject()),_imgfotoabdomen3.getLeft(),_imgfotoabdomen3.getTop(),_imgfotoabdomen3.getWidth(),(int) (_imgfotoabdomen3.getHeight()/(double)4));
 //BA.debugLineNum = 485;BA.debugLine="scrPartes.Panel.AddView(btnAbdomen3, 0, imgAbdome";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getObject()),(int) (0),_imgabdomen3.getTop(),mostCurrent._scrpartes.getWidth(),_imgabdomen3.getHeight());
 //BA.debugLineNum = 487;BA.debugLine="scrPartes.Panel.Height= imgAbdomen1.top + imgAbdo";
mostCurrent._scrpartes.getPanel().setHeight((int) (_imgabdomen1.getTop()+_imgabdomen1.getHeight()*3+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))));
 //BA.debugLineNum = 490;BA.debugLine="lblHighlight.Visible = True";
mostCurrent._lblhighlight.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 491;BA.debugLine="lblHighlight.Left = imgMosquito.left + (imgMosqui";
mostCurrent._lblhighlight.setLeft((int) (mostCurrent._imgmosquito.getLeft()+(mostCurrent._imgmosquito.getWidth()/(double)3)));
 //BA.debugLineNum = 492;BA.debugLine="lblHighlight.Top = imgMosquito.Top + (imgMosquito";
mostCurrent._lblhighlight.setTop((int) (mostCurrent._imgmosquito.getTop()+(mostCurrent._imgmosquito.getHeight()/(double)2.2)));
 //BA.debugLineNum = 493;BA.debugLine="lblHighlight.Width = imgMosquito.Width / 3";
mostCurrent._lblhighlight.setWidth((int) (mostCurrent._imgmosquito.getWidth()/(double)3));
 //BA.debugLineNum = 494;BA.debugLine="lblHighlight.height = imgMosquito.Height / 4";
mostCurrent._lblhighlight.setHeight((int) (mostCurrent._imgmosquito.getHeight()/(double)4));
 //BA.debugLineNum = 502;BA.debugLine="spnPatas.Visible = False";
mostCurrent._spnpatas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 503;BA.debugLine="spnTorax.Visible = False";
mostCurrent._spntorax.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 504;BA.debugLine="spnAbdomen.Visible = True";
mostCurrent._spnabdomen.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 505;BA.debugLine="spnCabeza.Visible = False";
mostCurrent._spncabeza.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 506;BA.debugLine="End Sub";
return "";
}
public static String  _lblcabeza_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgcabeza1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgcabeza2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgcabeza3 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _disabled = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _enabled = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld1 = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblcabeza1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblcabeza2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblcabeza3 = null;
int _i = 0;
 //BA.debugLineNum = 777;BA.debugLine="Sub lblCabeza_Click";
 //BA.debugLineNum = 778;BA.debugLine="lblPatas.Color = Colors.Transparent";
mostCurrent._lblpatas.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 779;BA.debugLine="lblPatas.TextColor = Colors.white";
mostCurrent._lblpatas.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 780;BA.debugLine="lblAbdomen.Color = Colors.Transparent";
mostCurrent._lblabdomen.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 781;BA.debugLine="lblAbdomen.TextColor = Colors.white";
mostCurrent._lblabdomen.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 782;BA.debugLine="lblCabeza.Color = Colors.white";
mostCurrent._lblcabeza.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 783;BA.debugLine="lblCabeza.TextColor = Colors.DarkGray";
mostCurrent._lblcabeza.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 784;BA.debugLine="lblTorax.Color = Colors.Transparent";
mostCurrent._lbltorax.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 785;BA.debugLine="lblTorax.TextColor = Colors.white";
mostCurrent._lbltorax.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 788;BA.debugLine="Dim imgCabeza1 As ImageView";
_imgcabeza1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 789;BA.debugLine="Dim imgCabeza2 As ImageView";
_imgcabeza2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 790;BA.debugLine="Dim imgCabeza3 As ImageView";
_imgcabeza3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 791;BA.debugLine="imgCabeza1.Initialize(\"\")";
_imgcabeza1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 792;BA.debugLine="imgCabeza2.Initialize(\"\")";
_imgcabeza2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 793;BA.debugLine="imgCabeza3.Initialize(\"\")";
_imgcabeza3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 794;BA.debugLine="imgCabeza1.Bitmap = LoadBitmap(File.DirAssets, \"c";
_imgcabeza1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza1-zoom.png").getObject()));
 //BA.debugLineNum = 795;BA.debugLine="imgCabeza2.Bitmap = LoadBitmap(File.DirAssets, \"c";
_imgcabeza2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza2-zoom.png").getObject()));
 //BA.debugLineNum = 796;BA.debugLine="imgCabeza3.Bitmap = LoadBitmap(File.DirAssets, \"c";
_imgcabeza3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"cabeza3-zoom.png").getObject()));
 //BA.debugLineNum = 797;BA.debugLine="imgCabeza1.Gravity = Gravity.CENTER_VERTICAL";
_imgcabeza1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 798;BA.debugLine="imgCabeza2.Gravity = Gravity.CENTER_VERTICAL";
_imgcabeza2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 799;BA.debugLine="imgCabeza3.Gravity = Gravity.CENTER_VERTICAL";
_imgcabeza3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 802;BA.debugLine="btnCabeza1.Initialize(\"btnCabeza1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"btnCabeza1");
 //BA.debugLineNum = 803;BA.debugLine="btnCabeza2.Initialize(\"btnCabeza2\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(mostCurrent.activityBA,"btnCabeza2");
 //BA.debugLineNum = 804;BA.debugLine="btnCabeza3.Initialize(\"btnCabeza3\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"btnCabeza3");
 //BA.debugLineNum = 806;BA.debugLine="Dim disabled, enabled As ColorDrawable";
_disabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
_enabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 807;BA.debugLine="disabled.Initialize(Colors.ARGB(100,53,132,210),";
_disabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (53),(int) (132),(int) (210)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 808;BA.debugLine="enabled.Initialize(Colors.Transparent, 10dip)";
_enabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 809;BA.debugLine="Dim sld, sld1, sld2 As StateListDrawable";
_sld = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld1 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld2 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
 //BA.debugLineNum = 810;BA.debugLine="sld.Initialize";
_sld.Initialize();
 //BA.debugLineNum = 811;BA.debugLine="sld1.Initialize";
_sld1.Initialize();
 //BA.debugLineNum = 812;BA.debugLine="sld2.Initialize";
_sld2.Initialize();
 //BA.debugLineNum = 813;BA.debugLine="sld.AddState(sld.State_Disabled, disabled)";
_sld.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 814;BA.debugLine="sld.AddState(sld.State_Enabled, enabled)";
_sld.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 815;BA.debugLine="sld1.AddState(sld.State_Disabled, disabled)";
_sld1.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 816;BA.debugLine="sld1.AddState(sld.State_Enabled, enabled)";
_sld1.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 817;BA.debugLine="sld2.AddState(sld.State_Disabled, disabled)";
_sld2.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 818;BA.debugLine="sld2.AddState(sld.State_Enabled, enabled)";
_sld2.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 819;BA.debugLine="btnCabeza1.Background = sld";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setBackground((android.graphics.drawable.Drawable)(_sld.getObject()));
 //BA.debugLineNum = 820;BA.debugLine="btnCabeza2.Background = sld1";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setBackground((android.graphics.drawable.Drawable)(_sld1.getObject()));
 //BA.debugLineNum = 821;BA.debugLine="btnCabeza3.Background = sld2";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBackground((android.graphics.drawable.Drawable)(_sld2.getObject()));
 //BA.debugLineNum = 823;BA.debugLine="btnNoSe.Text = \"No se que como es la cabeza\"";
mostCurrent._btnnose.setText(BA.ObjectToCharSequence("No se que como es la cabeza"));
 //BA.debugLineNum = 826;BA.debugLine="Dim lblCabeza1 As Label";
_lblcabeza1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 827;BA.debugLine="Dim lblCabeza2 As Label";
_lblcabeza2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 828;BA.debugLine="Dim lblCabeza3 As Label";
_lblcabeza3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 829;BA.debugLine="lblCabeza1.Initialize(\"\")";
_lblcabeza1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 830;BA.debugLine="lblCabeza2.Initialize(\"\")";
_lblcabeza2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 831;BA.debugLine="lblCabeza3.Initialize(\"\")";
_lblcabeza3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 832;BA.debugLine="lblCabeza1.Text = \"Con marcas blancas y patrón de";
_lblcabeza1.setText(BA.ObjectToCharSequence("Con marcas blancas y patrón de coloración"));
 //BA.debugLineNum = 833;BA.debugLine="lblCabeza2.Text = \"Con marcas blancas y una línea";
_lblcabeza2.setText(BA.ObjectToCharSequence("Con marcas blancas y una línea media"));
 //BA.debugLineNum = 834;BA.debugLine="lblCabeza3.Text = \"Sin marcas blancas\"";
_lblcabeza3.setText(BA.ObjectToCharSequence("Sin marcas blancas"));
 //BA.debugLineNum = 838;BA.debugLine="For i = scrPartes.Panel.NumberOfViews - 1 To 0 St";
{
final int step50 = -1;
final int limit50 = (int) (0);
_i = (int) (mostCurrent._scrpartes.getPanel().getNumberOfViews()-1) ;
for (;_i >= limit50 ;_i = _i + step50 ) {
 //BA.debugLineNum = 839;BA.debugLine="scrPartes.Panel.RemoveViewAt(i)";
mostCurrent._scrpartes.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 841;BA.debugLine="scrPartes.Panel.AddView(imgCabeza1, 10dip, 10dip,";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgcabeza1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (mostCurrent._scrpartes.getWidth()/(double)4),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 842;BA.debugLine="scrPartes.Panel.AddView(lblCabeza1, imgCabeza1.Le";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblcabeza1.getObject()),(int) (_imgcabeza1.getLeft()+_imgcabeza1.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgcabeza1.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgcabeza1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgcabeza1.getHeight());
 //BA.debugLineNum = 843;BA.debugLine="scrPartes.Panel.AddView(btnCabeza1, 0, imgCabeza1";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),(int) (0),_imgcabeza1.getTop(),mostCurrent._scrpartes.getWidth(),_imgcabeza1.getHeight());
 //BA.debugLineNum = 845;BA.debugLine="scrPartes.Panel.AddView(imgCabeza2, 10dip, imgCab";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgcabeza2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgcabeza1.getHeight()+_imgcabeza1.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)4),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 846;BA.debugLine="scrPartes.Panel.AddView(lblCabeza2, imgCabeza2.Le";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblcabeza2.getObject()),(int) (_imgcabeza2.getLeft()+_imgcabeza2.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgcabeza2.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgcabeza2.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgcabeza2.getHeight());
 //BA.debugLineNum = 847;BA.debugLine="scrPartes.Panel.AddView(btnCabeza2, 0, imgCabeza2";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()),(int) (0),_imgcabeza2.getTop(),mostCurrent._scrpartes.getWidth(),_imgcabeza2.getHeight());
 //BA.debugLineNum = 849;BA.debugLine="scrPartes.Panel.AddView(imgCabeza3, 10dip, imgCab";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgcabeza3.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgcabeza2.getHeight()+_imgcabeza2.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)4),(int) (mostCurrent._scrpartes.getHeight()/(double)1.5));
 //BA.debugLineNum = 850;BA.debugLine="scrPartes.Panel.AddView(lblCabeza3, imgCabeza3.Le";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblcabeza3.getObject()),(int) (_imgcabeza3.getLeft()+_imgcabeza3.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgcabeza3.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgcabeza3.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgcabeza3.getHeight());
 //BA.debugLineNum = 851;BA.debugLine="scrPartes.Panel.AddView(btnCabeza3, 0, imgCabeza3";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(int) (0),_imgcabeza3.getTop(),mostCurrent._scrpartes.getWidth(),_imgcabeza3.getHeight());
 //BA.debugLineNum = 852;BA.debugLine="scrPartes.Panel.Height= imgCabeza1.top + imgCabez";
mostCurrent._scrpartes.getPanel().setHeight((int) (_imgcabeza1.getTop()+_imgcabeza1.getHeight()*3+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))));
 //BA.debugLineNum = 856;BA.debugLine="lblHighlight.Visible = True";
mostCurrent._lblhighlight.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 857;BA.debugLine="lblHighlight.Left = imgMosquito.left + imgMosquit";
mostCurrent._lblhighlight.setLeft((int) (mostCurrent._imgmosquito.getLeft()+mostCurrent._imgmosquito.getWidth()/(double)2.6));
 //BA.debugLineNum = 858;BA.debugLine="lblHighlight.Top = imgMosquito.Top + imgMosquito.";
mostCurrent._lblhighlight.setTop((int) (mostCurrent._imgmosquito.getTop()+mostCurrent._imgmosquito.getHeight()/(double)6));
 //BA.debugLineNum = 859;BA.debugLine="lblHighlight.Width = imgMosquito.Width / 4";
mostCurrent._lblhighlight.setWidth((int) (mostCurrent._imgmosquito.getWidth()/(double)4));
 //BA.debugLineNum = 860;BA.debugLine="lblHighlight.height = imgMosquito.Width / 4";
mostCurrent._lblhighlight.setHeight((int) (mostCurrent._imgmosquito.getWidth()/(double)4));
 //BA.debugLineNum = 862;BA.debugLine="spnPatas.Visible = False";
mostCurrent._spnpatas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 863;BA.debugLine="spnTorax.Visible = False";
mostCurrent._spntorax.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 864;BA.debugLine="spnAbdomen.Visible = False";
mostCurrent._spnabdomen.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 865;BA.debugLine="spnCabeza.Visible = True";
mostCurrent._spncabeza.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 866;BA.debugLine="End Sub";
return "";
}
public static String  _lblpatas_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgpatas1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgpatas2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfotopatas1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfotopatas2 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _disabled = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _enabled = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblpatas1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lblpatas2 = null;
int _i = 0;
 //BA.debugLineNum = 257;BA.debugLine="Sub lblPatas_Click";
 //BA.debugLineNum = 258;BA.debugLine="lblPatas.Color = Colors.White";
mostCurrent._lblpatas.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 259;BA.debugLine="lblPatas.TextColor = Colors.DarkGray";
mostCurrent._lblpatas.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 260;BA.debugLine="lblAbdomen.Color = Colors.Transparent";
mostCurrent._lblabdomen.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 261;BA.debugLine="lblAbdomen.TextColor = Colors.white";
mostCurrent._lblabdomen.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 262;BA.debugLine="lblCabeza.Color = Colors.Transparent";
mostCurrent._lblcabeza.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 263;BA.debugLine="lblCabeza.TextColor = Colors.white";
mostCurrent._lblcabeza.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 264;BA.debugLine="lblTorax.Color = Colors.Transparent";
mostCurrent._lbltorax.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 265;BA.debugLine="lblTorax.TextColor = Colors.white";
mostCurrent._lbltorax.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 268;BA.debugLine="Dim imgPatas1 As ImageView";
_imgpatas1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 269;BA.debugLine="Dim imgPatas2 As ImageView";
_imgpatas2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 270;BA.debugLine="imgPatas1.Initialize(\"\")";
_imgpatas1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 271;BA.debugLine="imgPatas2.Initialize(\"\")";
_imgpatas2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 272;BA.debugLine="imgPatas1.Bitmap = LoadBitmap(File.DirAssets, \"pa";
_imgpatas1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas1.png").getObject()));
 //BA.debugLineNum = 273;BA.debugLine="imgPatas2.Bitmap = LoadBitmap(File.DirAssets, \"pa";
_imgpatas2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas2.png").getObject()));
 //BA.debugLineNum = 274;BA.debugLine="imgPatas1.Gravity = Gravity.Fill";
_imgpatas1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 275;BA.debugLine="imgPatas2.Gravity = Gravity.Fill";
_imgpatas2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 278;BA.debugLine="Dim imgFotoPatas1 As ImageView";
_imgfotopatas1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 279;BA.debugLine="Dim imgFotoPatas2 As ImageView";
_imgfotopatas2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 280;BA.debugLine="imgFotoPatas1.Initialize(\"\")";
_imgfotopatas1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 281;BA.debugLine="imgFotoPatas2.Initialize(\"\")";
_imgfotopatas2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 282;BA.debugLine="imgFotoPatas1.Bitmap = LoadBitmap(File.DirAssets,";
_imgfotopatas1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas1-foto.png").getObject()));
 //BA.debugLineNum = 283;BA.debugLine="imgFotoPatas2.Bitmap = LoadBitmap(File.DirAssets,";
_imgfotopatas2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"patas2-foto.png").getObject()));
 //BA.debugLineNum = 284;BA.debugLine="imgFotoPatas1.Gravity = Gravity.Fill";
_imgfotopatas1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 285;BA.debugLine="imgFotoPatas2.Gravity = Gravity.Fill";
_imgfotopatas2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 290;BA.debugLine="btnPatas1.Initialize(\"btnPatas1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(mostCurrent.activityBA,"btnPatas1");
 //BA.debugLineNum = 291;BA.debugLine="btnPatas2.Initialize(\"btnPatas2\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.Initialize(mostCurrent.activityBA,"btnPatas2");
 //BA.debugLineNum = 293;BA.debugLine="Dim disabled, enabled As ColorDrawable";
_disabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
_enabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 294;BA.debugLine="disabled.Initialize(Colors.ARGB(100,53,132,210),";
_disabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (53),(int) (132),(int) (210)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 295;BA.debugLine="enabled.Initialize(Colors.Transparent, 10dip)";
_enabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 296;BA.debugLine="Dim sld, sld1 As StateListDrawable";
_sld = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld1 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
 //BA.debugLineNum = 297;BA.debugLine="sld.Initialize";
_sld.Initialize();
 //BA.debugLineNum = 298;BA.debugLine="sld1.Initialize";
_sld1.Initialize();
 //BA.debugLineNum = 299;BA.debugLine="sld.AddState(sld.State_Disabled, disabled)";
_sld.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 300;BA.debugLine="sld.AddState(sld.State_Enabled, enabled)";
_sld.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 301;BA.debugLine="sld1.AddState(sld.State_Disabled, disabled)";
_sld1.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 302;BA.debugLine="sld1.AddState(sld.State_Enabled, enabled)";
_sld1.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 303;BA.debugLine="btnPatas1.Background = sld";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setBackground((android.graphics.drawable.Drawable)(_sld.getObject()));
 //BA.debugLineNum = 304;BA.debugLine="btnPatas2.Background = sld1";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setBackground((android.graphics.drawable.Drawable)(_sld1.getObject()));
 //BA.debugLineNum = 306;BA.debugLine="btnNoSe.Text = \"No se que como son las patas\"";
mostCurrent._btnnose.setText(BA.ObjectToCharSequence("No se que como son las patas"));
 //BA.debugLineNum = 309;BA.debugLine="Dim lblPatas1 As Label";
_lblpatas1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 310;BA.debugLine="Dim lblPatas2 As Label";
_lblpatas2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 311;BA.debugLine="lblPatas1.Initialize(\"\")";
_lblpatas1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 312;BA.debugLine="lblPatas2.Initialize(\"\")";
_lblpatas2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 313;BA.debugLine="lblPatas1.Text = \"Con rayas blancas\"";
_lblpatas1.setText(BA.ObjectToCharSequence("Con rayas blancas"));
 //BA.debugLineNum = 314;BA.debugLine="lblPatas2.Text = \"Sin rayas blancas\"";
_lblpatas2.setText(BA.ObjectToCharSequence("Sin rayas blancas"));
 //BA.debugLineNum = 315;BA.debugLine="lblPatas1.Color = Colors.ARGB(150,255,255,255)";
_lblpatas1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 316;BA.debugLine="lblPatas2.Color = Colors.ARGB(150,255,255,255)";
_lblpatas2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 317;BA.debugLine="lblPatas1.TextColor = Colors.Black";
_lblpatas1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 318;BA.debugLine="lblPatas2.TextColor = Colors.Black";
_lblpatas2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 319;BA.debugLine="lblPatas1.Gravity = Gravity.CENTER_HORIZONTAL";
_lblpatas1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 320;BA.debugLine="lblPatas2.Gravity = Gravity.CENTER_HORIZONTAL";
_lblpatas2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 323;BA.debugLine="For i = scrPartes.Panel.NumberOfViews - 1 To 0 St";
{
final int step52 = -1;
final int limit52 = (int) (0);
_i = (int) (mostCurrent._scrpartes.getPanel().getNumberOfViews()-1) ;
for (;_i >= limit52 ;_i = _i + step52 ) {
 //BA.debugLineNum = 324;BA.debugLine="scrPartes.Panel.RemoveViewAt(i)";
mostCurrent._scrpartes.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 327;BA.debugLine="scrPartes.Panel.AddView(imgPatas1, 10dip, 10dip,";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgpatas1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (mostCurrent._scrpartes.getWidth()/(double)3),(int) (mostCurrent._scrpartes.getHeight()/(double)2));
 //BA.debugLineNum = 328;BA.debugLine="scrPartes.Panel.AddView(imgFotoPatas1, imgPatas1.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfotopatas1.getObject()),(int) (_imgpatas1.getLeft()+_imgpatas1.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgpatas1.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgpatas1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgpatas1.getHeight());
 //BA.debugLineNum = 329;BA.debugLine="scrPartes.Panel.AddView(lblPatas1, imgFotoPatas1.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblpatas1.getObject()),_imgfotopatas1.getLeft(),_imgfotopatas1.getTop(),_imgfotopatas1.getWidth(),(int) (_imgfotopatas1.getHeight()/(double)4));
 //BA.debugLineNum = 330;BA.debugLine="scrPartes.Panel.AddView(btnPatas1, 0, imgPatas1.t";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getObject()),(int) (0),_imgpatas1.getTop(),mostCurrent._scrpartes.getWidth(),_imgpatas1.getHeight());
 //BA.debugLineNum = 332;BA.debugLine="scrPartes.Panel.AddView(imgPatas2, 10dip, imgPata";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgpatas2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgpatas1.getHeight()+_imgpatas1.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)3),(int) (mostCurrent._scrpartes.getHeight()/(double)2));
 //BA.debugLineNum = 333;BA.debugLine="scrPartes.Panel.AddView(imgFotoPatas2, imgPatas2.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfotopatas2.getObject()),(int) (_imgpatas2.getLeft()+_imgpatas2.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgpatas2.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgpatas2.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgpatas2.getHeight());
 //BA.debugLineNum = 334;BA.debugLine="scrPartes.Panel.AddView(lblPatas2, imgFotoPatas2.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lblpatas2.getObject()),_imgfotopatas2.getLeft(),_imgfotopatas2.getTop(),_imgfotopatas2.getWidth(),(int) (_imgfotopatas2.getHeight()/(double)4));
 //BA.debugLineNum = 335;BA.debugLine="scrPartes.Panel.AddView(btnPatas2, 0, imgPatas2.T";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getObject()),(int) (0),_imgpatas2.getTop(),mostCurrent._scrpartes.getWidth(),_imgpatas2.getHeight());
 //BA.debugLineNum = 337;BA.debugLine="scrPartes.Panel.Height= imgPatas1.top + imgPatas2";
mostCurrent._scrpartes.getPanel().setHeight((int) (_imgpatas1.getTop()+_imgpatas2.getHeight()*2+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))));
 //BA.debugLineNum = 340;BA.debugLine="lblHighlight.Visible = True";
mostCurrent._lblhighlight.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 341;BA.debugLine="lblHighlight.Left = imgMosquito.Left";
mostCurrent._lblhighlight.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 342;BA.debugLine="lblHighlight.Top = imgMosquito.Top";
mostCurrent._lblhighlight.setTop(mostCurrent._imgmosquito.getTop());
 //BA.debugLineNum = 343;BA.debugLine="lblHighlight.Width = imgMosquito.Width / 2.5";
mostCurrent._lblhighlight.setWidth((int) (mostCurrent._imgmosquito.getWidth()/(double)2.5));
 //BA.debugLineNum = 344;BA.debugLine="lblHighlight.height = imgMosquito.height";
mostCurrent._lblhighlight.setHeight(mostCurrent._imgmosquito.getHeight());
 //BA.debugLineNum = 354;BA.debugLine="spnPatas.Visible = True";
mostCurrent._spnpatas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 355;BA.debugLine="spnTorax.Visible = False";
mostCurrent._spntorax.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 356;BA.debugLine="spnAbdomen.Visible = False";
mostCurrent._spnabdomen.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 357;BA.debugLine="spnCabeza.Visible = False";
mostCurrent._spncabeza.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 358;BA.debugLine="End Sub";
return "";
}
public static String  _lbltorax_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgtorax1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgtorax2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgtorax3 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgtorax4 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfototorax1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfototorax2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfototorax3 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _imgfototorax4 = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _disabled = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _enabled = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld1 = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld2 = null;
anywheresoftware.b4a.objects.drawable.StateListDrawable _sld3 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbltorax1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbltorax2 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbltorax3 = null;
anywheresoftware.b4a.objects.LabelWrapper _lbltorax4 = null;
int _i = 0;
 //BA.debugLineNum = 548;BA.debugLine="Sub lblTorax_Click";
 //BA.debugLineNum = 549;BA.debugLine="lblPatas.Color = Colors.Transparent";
mostCurrent._lblpatas.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 550;BA.debugLine="lblPatas.TextColor = Colors.white";
mostCurrent._lblpatas.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 551;BA.debugLine="lblAbdomen.Color = Colors.Transparent";
mostCurrent._lblabdomen.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 552;BA.debugLine="lblAbdomen.TextColor = Colors.white";
mostCurrent._lblabdomen.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 553;BA.debugLine="lblCabeza.Color = Colors.Transparent";
mostCurrent._lblcabeza.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 554;BA.debugLine="lblCabeza.TextColor = Colors.white";
mostCurrent._lblcabeza.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 555;BA.debugLine="lblTorax.Color = Colors.White";
mostCurrent._lbltorax.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 556;BA.debugLine="lblTorax.TextColor = Colors.DarkGray";
mostCurrent._lbltorax.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 559;BA.debugLine="Dim imgTorax1 As ImageView";
_imgtorax1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 560;BA.debugLine="Dim imgTorax2 As ImageView";
_imgtorax2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 561;BA.debugLine="Dim imgTorax3 As ImageView";
_imgtorax3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 562;BA.debugLine="Dim imgTorax4 As ImageView";
_imgtorax4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 563;BA.debugLine="imgTorax1.Initialize(\"\")";
_imgtorax1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 564;BA.debugLine="imgTorax2.Initialize(\"\")";
_imgtorax2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 565;BA.debugLine="imgTorax3.Initialize(\"\")";
_imgtorax3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 566;BA.debugLine="imgTorax4.Initialize(\"\")";
_imgtorax4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 567;BA.debugLine="imgTorax1.Bitmap = LoadBitmap(File.DirAssets, \"to";
_imgtorax1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax1-zoom.png").getObject()));
 //BA.debugLineNum = 568;BA.debugLine="imgTorax2.Bitmap = LoadBitmap(File.DirAssets, \"to";
_imgtorax2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax2-zoom.png").getObject()));
 //BA.debugLineNum = 569;BA.debugLine="imgTorax3.Bitmap = LoadBitmap(File.DirAssets, \"to";
_imgtorax3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax3-zoom.png").getObject()));
 //BA.debugLineNum = 570;BA.debugLine="imgTorax4.Bitmap = LoadBitmap(File.DirAssets, \"to";
_imgtorax4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax4-zoom.png").getObject()));
 //BA.debugLineNum = 571;BA.debugLine="imgTorax1.Gravity =Gravity.CENTER_VERTICAL";
_imgtorax1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 572;BA.debugLine="imgTorax2.Gravity =Gravity.CENTER_VERTICAL";
_imgtorax2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 573;BA.debugLine="imgTorax3.Gravity =Gravity.CENTER_VERTICAL";
_imgtorax3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 574;BA.debugLine="imgTorax4.Gravity =Gravity.CENTER_VERTICAL";
_imgtorax4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 577;BA.debugLine="Dim imgFotoTorax1 As ImageView";
_imgfototorax1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 578;BA.debugLine="Dim imgFotoTorax2 As ImageView";
_imgfototorax2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 579;BA.debugLine="Dim imgFotoTorax3 As ImageView";
_imgfototorax3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 580;BA.debugLine="Dim imgFotoTorax4 As ImageView";
_imgfototorax4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 581;BA.debugLine="imgFotoTorax1.Initialize(\"\")";
_imgfototorax1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 582;BA.debugLine="imgFotoTorax2.Initialize(\"\")";
_imgfototorax2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 583;BA.debugLine="imgFotoTorax3.Initialize(\"\")";
_imgfototorax3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 584;BA.debugLine="imgFotoTorax4.Initialize(\"\")";
_imgfototorax4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 585;BA.debugLine="imgFotoTorax1.Bitmap = LoadBitmap(File.DirAssets,";
_imgfototorax1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax1-foto.png").getObject()));
 //BA.debugLineNum = 586;BA.debugLine="imgFotoTorax2.Bitmap = LoadBitmap(File.DirAssets,";
_imgfototorax2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax2-foto.png").getObject()));
 //BA.debugLineNum = 587;BA.debugLine="imgFotoTorax3.Bitmap = LoadBitmap(File.DirAssets,";
_imgfototorax3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax3-foto.png").getObject()));
 //BA.debugLineNum = 588;BA.debugLine="imgFotoTorax4.Bitmap = LoadBitmap(File.DirAssets,";
_imgfototorax4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"torax4-foto.png").getObject()));
 //BA.debugLineNum = 589;BA.debugLine="imgFotoTorax1.Gravity = Gravity.Fill";
_imgfototorax1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 590;BA.debugLine="imgFotoTorax2.Gravity = Gravity.Fill";
_imgfototorax2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 591;BA.debugLine="imgFotoTorax3.Gravity = Gravity.Fill";
_imgfototorax3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 592;BA.debugLine="imgFotoTorax4.Gravity = Gravity.Fill";
_imgfototorax4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 597;BA.debugLine="btnTorax1.Initialize(\"btnTorax1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"btnTorax1");
 //BA.debugLineNum = 598;BA.debugLine="btnTorax2.Initialize(\"btnTorax2\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"btnTorax2");
 //BA.debugLineNum = 599;BA.debugLine="btnTorax3.Initialize(\"btnTorax3\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(mostCurrent.activityBA,"btnTorax3");
 //BA.debugLineNum = 600;BA.debugLine="btnTorax4.Initialize(\"btnTorax4\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"btnTorax4");
 //BA.debugLineNum = 602;BA.debugLine="Dim disabled, enabled As ColorDrawable";
_disabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
_enabled = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 603;BA.debugLine="disabled.Initialize(Colors.ARGB(100,53,132,210),";
_disabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (53),(int) (132),(int) (210)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 604;BA.debugLine="enabled.Initialize(Colors.Transparent, 10dip)";
_enabled.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 605;BA.debugLine="Dim sld, sld1, sld2, sld3 As StateListDrawable";
_sld = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld1 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld2 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
_sld3 = new anywheresoftware.b4a.objects.drawable.StateListDrawable();
 //BA.debugLineNum = 606;BA.debugLine="sld.Initialize";
_sld.Initialize();
 //BA.debugLineNum = 607;BA.debugLine="sld1.Initialize";
_sld1.Initialize();
 //BA.debugLineNum = 608;BA.debugLine="sld2.Initialize";
_sld2.Initialize();
 //BA.debugLineNum = 609;BA.debugLine="sld3.Initialize";
_sld3.Initialize();
 //BA.debugLineNum = 610;BA.debugLine="sld.AddState(sld.State_Disabled, disabled)";
_sld.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 611;BA.debugLine="sld.AddState(sld.State_Enabled, enabled)";
_sld.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 612;BA.debugLine="sld1.AddState(sld.State_Disabled, disabled)";
_sld1.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 613;BA.debugLine="sld1.AddState(sld.State_Enabled, enabled)";
_sld1.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 614;BA.debugLine="sld2.AddState(sld.State_Disabled, disabled)";
_sld2.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 615;BA.debugLine="sld2.AddState(sld.State_Enabled, enabled)";
_sld2.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 616;BA.debugLine="sld3.AddState(sld.State_Disabled, disabled)";
_sld3.AddState(_sld.State_Disabled,(android.graphics.drawable.Drawable)(_disabled.getObject()));
 //BA.debugLineNum = 617;BA.debugLine="sld3.AddState(sld.State_Enabled, enabled)";
_sld3.AddState(_sld.State_Enabled,(android.graphics.drawable.Drawable)(_enabled.getObject()));
 //BA.debugLineNum = 618;BA.debugLine="btnTorax1.Background = sld";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBackground((android.graphics.drawable.Drawable)(_sld.getObject()));
 //BA.debugLineNum = 619;BA.debugLine="btnTorax2.Background = sld1";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setBackground((android.graphics.drawable.Drawable)(_sld1.getObject()));
 //BA.debugLineNum = 620;BA.debugLine="btnTorax3.Background = sld2";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setBackground((android.graphics.drawable.Drawable)(_sld2.getObject()));
 //BA.debugLineNum = 621;BA.debugLine="btnTorax4.Background = sld3";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBackground((android.graphics.drawable.Drawable)(_sld3.getObject()));
 //BA.debugLineNum = 623;BA.debugLine="btnNoSe.Text = \"No se que como es el tórax\"";
mostCurrent._btnnose.setText(BA.ObjectToCharSequence("No se que como es el tórax"));
 //BA.debugLineNum = 626;BA.debugLine="Dim lblTorax1 As Label";
_lbltorax1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 627;BA.debugLine="Dim lblTorax2 As Label";
_lbltorax2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 628;BA.debugLine="Dim lblTorax3 As Label";
_lbltorax3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 629;BA.debugLine="Dim lblTorax4 As Label";
_lbltorax4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 631;BA.debugLine="lblTorax1.Initialize(\"\")";
_lbltorax1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 632;BA.debugLine="lblTorax2.Initialize(\"\")";
_lbltorax2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 633;BA.debugLine="lblTorax3.Initialize(\"\")";
_lbltorax3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 634;BA.debugLine="lblTorax4.Initialize(\"\")";
_lbltorax4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 635;BA.debugLine="lblTorax1.Text = \"Con marcas blancas y dibujo en";
_lbltorax1.setText(BA.ObjectToCharSequence("Con marcas blancas y dibujo en forma de lira"));
 //BA.debugLineNum = 636;BA.debugLine="lblTorax2.Text = \"Con marcas blancas y una línea";
_lbltorax2.setText(BA.ObjectToCharSequence("Con marcas blancas y una línea media dorsal"));
 //BA.debugLineNum = 637;BA.debugLine="lblTorax3.Text = \"Sin marcas blancas y un patrón";
_lbltorax3.setText(BA.ObjectToCharSequence("Sin marcas blancas y un patrón de coloración"));
 //BA.debugLineNum = 638;BA.debugLine="lblTorax4.Text = \"Sin marcas blancas y color unif";
_lbltorax4.setText(BA.ObjectToCharSequence("Sin marcas blancas y color uniforme"));
 //BA.debugLineNum = 639;BA.debugLine="lblTorax1.Color = Colors.ARGB(150,255,255,255)";
_lbltorax1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 640;BA.debugLine="lblTorax2.Color = Colors.ARGB(150,255,255,255)";
_lbltorax2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 641;BA.debugLine="lblTorax3.Color = Colors.ARGB(150,255,255,255)";
_lbltorax3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 642;BA.debugLine="lblTorax4.Color = Colors.ARGB(150,255,255,255)";
_lbltorax4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 643;BA.debugLine="lblTorax1.TextColor = Colors.Black";
_lbltorax1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 644;BA.debugLine="lblTorax2.TextColor = Colors.Black";
_lbltorax2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 645;BA.debugLine="lblTorax3.TextColor = Colors.Black";
_lbltorax3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 646;BA.debugLine="lblTorax4.TextColor = Colors.Black";
_lbltorax4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 647;BA.debugLine="lblTorax1.TextSize = 12";
_lbltorax1.setTextSize((float) (12));
 //BA.debugLineNum = 648;BA.debugLine="lblTorax2.TextSize = 12";
_lbltorax2.setTextSize((float) (12));
 //BA.debugLineNum = 649;BA.debugLine="lblTorax3.TextSize = 12";
_lbltorax3.setTextSize((float) (12));
 //BA.debugLineNum = 650;BA.debugLine="lblTorax4.TextSize = 12";
_lbltorax4.setTextSize((float) (12));
 //BA.debugLineNum = 651;BA.debugLine="lblTorax1.Gravity = Gravity.CENTER_HORIZONTAL";
_lbltorax1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 652;BA.debugLine="lblTorax2.Gravity = Gravity.CENTER_HORIZONTAL";
_lbltorax2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 653;BA.debugLine="lblTorax3.Gravity = Gravity.CENTER_HORIZONTAL";
_lbltorax3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 654;BA.debugLine="lblTorax4.Gravity = Gravity.CENTER_HORIZONTAL";
_lbltorax4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 658;BA.debugLine="For i = scrPartes.Panel.NumberOfViews - 1 To 0 St";
{
final int step94 = -1;
final int limit94 = (int) (0);
_i = (int) (mostCurrent._scrpartes.getPanel().getNumberOfViews()-1) ;
for (;_i >= limit94 ;_i = _i + step94 ) {
 //BA.debugLineNum = 659;BA.debugLine="scrPartes.Panel.RemoveViewAt(i)";
mostCurrent._scrpartes.getPanel().RemoveViewAt(_i);
 }
};
 //BA.debugLineNum = 661;BA.debugLine="scrPartes.Panel.AddView(imgTorax1, 10dip, 10dip,";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgtorax1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (mostCurrent._scrpartes.getWidth()/(double)2.5),(int) (mostCurrent._scrpartes.getHeight()/(double)1.7));
 //BA.debugLineNum = 662;BA.debugLine="scrPartes.Panel.AddView(imgFotoTorax1, imgTorax1.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfototorax1.getObject()),(int) (_imgtorax1.getLeft()+_imgtorax1.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),_imgtorax1.getTop(),(int) (mostCurrent._scrpartes.getWidth()-_imgtorax1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),_imgtorax1.getHeight());
 //BA.debugLineNum = 663;BA.debugLine="scrPartes.Panel.AddView(lblTorax1, imgFotoTorax1.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lbltorax1.getObject()),_imgfototorax1.getLeft(),_imgfototorax1.getTop(),_imgfototorax1.getWidth(),(int) (_imgfototorax1.getHeight()/(double)4));
 //BA.debugLineNum = 664;BA.debugLine="scrPartes.Panel.AddView(btnTorax1, 0, imgTorax1.t";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) (0),_imgtorax1.getTop(),mostCurrent._scrpartes.getWidth(),_imgtorax1.getHeight());
 //BA.debugLineNum = 666;BA.debugLine="scrPartes.Panel.AddView(imgTorax2, 10dip, imgTora";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgtorax2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgtorax1.getHeight()+_imgtorax1.getTop()),(int) (mostCurrent._scrpartes.getWidth()/(double)2.5),(int) (mostCurrent._scrpartes.getHeight()/(double)1.7));
 //BA.debugLineNum = 667;BA.debugLine="scrPartes.Panel.AddView(imgFotoTorax2, imgTorax2.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfototorax2.getObject()),(int) (_imgtorax2.getLeft()+_imgtorax2.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (_imgtorax2.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()-_imgtorax2.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),(int) (_imgtorax2.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 668;BA.debugLine="scrPartes.Panel.AddView(lblTorax2, imgFotoTorax2.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lbltorax2.getObject()),_imgfototorax2.getLeft(),_imgfototorax2.getTop(),_imgfototorax2.getWidth(),(int) (_imgfototorax2.getHeight()/(double)4));
 //BA.debugLineNum = 669;BA.debugLine="scrPartes.Panel.AddView(btnTorax2, 0, imgTorax2.T";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),(int) (0),_imgtorax2.getTop(),mostCurrent._scrpartes.getWidth(),_imgtorax2.getHeight());
 //BA.debugLineNum = 671;BA.debugLine="scrPartes.Panel.AddView(imgTorax3, 10dip, imgTora";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgtorax3.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgtorax2.getHeight()+_imgtorax2.getTop()),(int) (mostCurrent._scrpartes.getWidth()/(double)2.5),(int) (mostCurrent._scrpartes.getHeight()/(double)1.7));
 //BA.debugLineNum = 672;BA.debugLine="scrPartes.Panel.AddView(imgFotoTorax3, imgTorax3.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfototorax3.getObject()),(int) (_imgtorax3.getLeft()+_imgtorax3.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (_imgtorax3.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()-_imgtorax3.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),(int) (_imgtorax3.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 673;BA.debugLine="scrPartes.Panel.AddView(lblTorax3, imgFotoTorax3.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lbltorax3.getObject()),_imgfototorax3.getLeft(),_imgfototorax3.getTop(),_imgfototorax3.getWidth(),(int) (_imgfototorax3.getHeight()/(double)4));
 //BA.debugLineNum = 674;BA.debugLine="scrPartes.Panel.AddView(btnTorax3, 0, imgTorax3.T";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()),(int) (0),_imgtorax3.getTop(),mostCurrent._scrpartes.getWidth(),_imgtorax3.getHeight());
 //BA.debugLineNum = 676;BA.debugLine="scrPartes.Panel.AddView(imgTorax4, 10dip, imgTora";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgtorax4.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) (_imgtorax3.getHeight()+_imgtorax3.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()/(double)2.5),(int) (mostCurrent._scrpartes.getHeight()/(double)1.7));
 //BA.debugLineNum = 677;BA.debugLine="scrPartes.Panel.AddView(imgFotoTorax4, imgTorax4.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_imgfototorax4.getObject()),(int) (_imgtorax4.getLeft()+_imgtorax4.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (_imgtorax4.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),(int) (mostCurrent._scrpartes.getWidth()-_imgtorax4.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))),(int) (_imgtorax4.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 678;BA.debugLine="scrPartes.Panel.AddView(lblTorax4, imgFotoTorax4.";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(_lbltorax4.getObject()),_imgfototorax4.getLeft(),_imgfototorax4.getTop(),_imgfototorax4.getWidth(),(int) (_imgfototorax4.getHeight()/(double)4));
 //BA.debugLineNum = 679;BA.debugLine="scrPartes.Panel.AddView(btnTorax4, 0, imgTorax4.T";
mostCurrent._scrpartes.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(int) (0),_imgtorax4.getTop(),mostCurrent._scrpartes.getWidth(),_imgtorax4.getHeight());
 //BA.debugLineNum = 681;BA.debugLine="scrPartes.Panel.Height= imgTorax1.top + imgTorax1";
mostCurrent._scrpartes.getPanel().setHeight((int) (_imgtorax1.getTop()+_imgtorax1.getHeight()*4+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30))));
 //BA.debugLineNum = 684;BA.debugLine="lblHighlight.Visible = True";
mostCurrent._lblhighlight.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 685;BA.debugLine="lblHighlight.Left = imgMosquito.left";
mostCurrent._lblhighlight.setLeft(mostCurrent._imgmosquito.getLeft());
 //BA.debugLineNum = 686;BA.debugLine="lblHighlight.Top = imgMosquito.Top + imgMosquito.";
mostCurrent._lblhighlight.setTop((int) (mostCurrent._imgmosquito.getTop()+mostCurrent._imgmosquito.getHeight()/(double)3));
 //BA.debugLineNum = 687;BA.debugLine="lblHighlight.Width = imgMosquito.Width";
mostCurrent._lblhighlight.setWidth(mostCurrent._imgmosquito.getWidth());
 //BA.debugLineNum = 688;BA.debugLine="lblHighlight.height = imgMosquito.Height / 6.5";
mostCurrent._lblhighlight.setHeight((int) (mostCurrent._imgmosquito.getHeight()/(double)6.5));
 //BA.debugLineNum = 713;BA.debugLine="spnPatas.Visible = False";
mostCurrent._spnpatas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 714;BA.debugLine="spnTorax.Visible = True";
mostCurrent._spntorax.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 715;BA.debugLine="spnAbdomen.Visible = False";
mostCurrent._spnabdomen.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 716;BA.debugLine="spnCabeza.Visible = False";
mostCurrent._spncabeza.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 717;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_gesture_onpinchclose(float _newdistance,float _previousdistance,Object _motionevent) throws Exception{
 //BA.debugLineNum = 1148;BA.debugLine="Sub pnl_gesture_onPinchClose(NewDistance As Float,";
 //BA.debugLineNum = 1149;BA.debugLine="zoom = zoom *(NewDistance/PreviousDistance)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvv0 = (float) (_vvvvvvvvvvvvvvvvvvvvvvvvvvv0*(_newdistance/(double)_previousdistance));
 //BA.debugLineNum = 1150;BA.debugLine="imgUsuario.Width = bmp.Width*zoom";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setWidth((int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getWidth()*_vvvvvvvvvvvvvvvvvvvvvvvvvvv0));
 //BA.debugLineNum = 1151;BA.debugLine="imgUsuario.Height = bmp.Height*zoom";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setHeight((int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getHeight()*_vvvvvvvvvvvvvvvvvvvvvvvvvvv0));
 //BA.debugLineNum = 1152;BA.debugLine="imgUsuario.Top = (scroll2d.Panel.Height-imgUsuari";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setTop((int) ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().getHeight()-mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getHeight())/(double)3));
 //BA.debugLineNum = 1153;BA.debugLine="imgUsuario.left = (scroll2d.Panel.Width - imgUsua";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setLeft((int) ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().getWidth()-mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth())/(double)3));
 //BA.debugLineNum = 1154;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_gesture_onpinchopen(float _newdistance,float _previousdistance,Object _motionevent) throws Exception{
 //BA.debugLineNum = 1141;BA.debugLine="Sub pnl_gesture_onPinchOpen(NewDistance As Float,";
 //BA.debugLineNum = 1142;BA.debugLine="zoom = zoom * (NewDistance/PreviousDistance)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvv0 = (float) (_vvvvvvvvvvvvvvvvvvvvvvvvvvv0*(_newdistance/(double)_previousdistance));
 //BA.debugLineNum = 1143;BA.debugLine="imgUsuario.Width = bmp.Width*zoom";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setWidth((int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getWidth()*_vvvvvvvvvvvvvvvvvvvvvvvvvvv0));
 //BA.debugLineNum = 1144;BA.debugLine="imgUsuario.Height = bmp.Height*zoom";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setHeight((int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getHeight()*_vvvvvvvvvvvvvvvvvvvvvvvvvvv0));
 //BA.debugLineNum = 1145;BA.debugLine="imgUsuario.Top = (scroll2d.Panel.Height-imgUsuari";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setTop((int) ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().getHeight()-mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getHeight())/(double)3));
 //BA.debugLineNum = 1146;BA.debugLine="imgUsuario.left = (scroll2d.Panel.Width - imgUsua";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.setLeft((int) ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getPanel().getWidth()-mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth())/(double)3));
 //BA.debugLineNum = 1147;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim p As Phone";
_vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 20;BA.debugLine="Dim currentPregunta As Int";
_vvvvvvv7 = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim foto1 As String";
_vvvvvvv0 = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim foto2 As String";
_vvvvvvvv1 = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim foto3 As String";
_vvvvvvvv2 = "";
 //BA.debugLineNum = 25;BA.debugLine="Dim foto4 As String";
_vvvvvvvv3 = "";
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _spnespecie_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1004;BA.debugLine="Sub spnEspecie_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 1006;BA.debugLine="If spnEspecie.SelectedIndex = 0 Then";
if (mostCurrent._spnespecie.getSelectedIndex()==0) { 
 //BA.debugLineNum = 1007;BA.debugLine="spnCabeza.SelectedIndex = 1";
mostCurrent._spncabeza.setSelectedIndex((int) (1));
 //BA.debugLineNum = 1008;BA.debugLine="spnTorax.SelectedIndex = 1";
mostCurrent._spntorax.setSelectedIndex((int) (1));
 //BA.debugLineNum = 1009;BA.debugLine="spnAbdomen.SelectedIndex = 1";
mostCurrent._spnabdomen.setSelectedIndex((int) (1));
 //BA.debugLineNum = 1010;BA.debugLine="spnPatas.SelectedIndex = 1";
mostCurrent._spnpatas.setSelectedIndex((int) (1));
 }else if(mostCurrent._spnespecie.getSelectedIndex()==2) { 
 //BA.debugLineNum = 1012;BA.debugLine="spnCabeza.SelectedIndex = 2";
mostCurrent._spncabeza.setSelectedIndex((int) (2));
 //BA.debugLineNum = 1013;BA.debugLine="spnTorax.SelectedIndex = 2";
mostCurrent._spntorax.setSelectedIndex((int) (2));
 //BA.debugLineNum = 1014;BA.debugLine="spnAbdomen.SelectedIndex = 1";
mostCurrent._spnabdomen.setSelectedIndex((int) (1));
 //BA.debugLineNum = 1015;BA.debugLine="spnPatas.SelectedIndex = 1";
mostCurrent._spnpatas.setSelectedIndex((int) (1));
 }else if(mostCurrent._spnespecie.getSelectedIndex()==1) { 
 //BA.debugLineNum = 1017;BA.debugLine="spnCabeza.SelectedIndex = 3";
mostCurrent._spncabeza.setSelectedIndex((int) (3));
 //BA.debugLineNum = 1018;BA.debugLine="spnTorax.SelectedIndex = 3";
mostCurrent._spntorax.setSelectedIndex((int) (3));
 //BA.debugLineNum = 1019;BA.debugLine="spnAbdomen.SelectedIndex = 2";
mostCurrent._spnabdomen.setSelectedIndex((int) (2));
 //BA.debugLineNum = 1020;BA.debugLine="spnPatas.SelectedIndex = 2";
mostCurrent._spnpatas.setSelectedIndex((int) (2));
 }else if(mostCurrent._spnespecie.getSelectedIndex()==3) { 
 //BA.debugLineNum = 1022;BA.debugLine="spnCabeza.SelectedIndex = 3";
mostCurrent._spncabeza.setSelectedIndex((int) (3));
 //BA.debugLineNum = 1023;BA.debugLine="spnTorax.SelectedIndex = 4";
mostCurrent._spntorax.setSelectedIndex((int) (4));
 //BA.debugLineNum = 1024;BA.debugLine="spnAbdomen.SelectedIndex = 3";
mostCurrent._spnabdomen.setSelectedIndex((int) (3));
 //BA.debugLineNum = 1025;BA.debugLine="spnPatas.SelectedIndex = 2";
mostCurrent._spnpatas.setSelectedIndex((int) (2));
 };
 //BA.debugLineNum = 1027;BA.debugLine="lblHighlight.Visible = False";
mostCurrent._lblhighlight.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1028;BA.debugLine="CargarPartesMosquito";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 1030;BA.debugLine="End Sub";
return "";
}
}
