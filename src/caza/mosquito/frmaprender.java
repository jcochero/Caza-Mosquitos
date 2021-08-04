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

public class frmaprender extends Activity implements B4AActivity{
	public static frmaprender mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmaprender");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmaprender).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmaprender");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmaprender", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmaprender) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmaprender) Resume **");
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
		return frmaprender.class;
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
            BA.LogInfo("** Activity (frmaprender) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmaprender) Pause event (activity is not paused). **");
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
            frmaprender mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmaprender) Resume **");
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
public static String _vvvvv7 = "";
public anywheresoftware.b4a.objects.LabelWrapper _imgcirculo = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnquecriadero = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncicloaedes = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblencontrecriadero = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imglupa = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnquemosquito = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblaprendermosquito = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgenfermedades = null;
public anywheresoftware.b4a.objects.ButtonWrapper _lblexplorarmapa2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblmosquitoaedes = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquitoicon = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmanos = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnenfermedades = null;
public anywheresoftware.b4a.phone.Phone _vvvvvvv6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnvolveraprender = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerraraprender = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.starter _vvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomofotos _vvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmlogin _vvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.dbutils _vvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.downloadservice _vvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.firebasemessaging _vvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmabout _vvvvvvvvvvvvvvvvvvv2 = null;
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
public caza.mosquito.frmidentificarmosquito _vvvvvvvvvvvvvvvvvvvv5 = null;
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
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 46;BA.debugLine="Activity.LoadLayout(\"layAprenderCM2\")";
mostCurrent._activity.LoadLayout("layAprenderCM2",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 59;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 60;BA.debugLine="If btnVolverAprender.Visible = True Then";
if (mostCurrent._btnvolveraprender.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 61;BA.debugLine="btnVolverAprender_Click";
_btnvolveraprender_click();
 //BA.debugLineNum = 62;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 64;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 65;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 66;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else {
 //BA.debugLineNum = 69;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 51;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._vvvvvvv6.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _btncerraraprender_click() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub btnCerrarAprender_Click";
 //BA.debugLineNum = 176;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 177;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _btncicloaedes_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgcriadero = null;
 //BA.debugLineNum = 100;BA.debugLine="Sub btnCicloAedes_Click";
 //BA.debugLineNum = 103;BA.debugLine="btnQueCriadero.Visible = False";
mostCurrent._btnquecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 104;BA.debugLine="btnCicloAedes.Visible = False";
mostCurrent._btncicloaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 105;BA.debugLine="lblEncontreCriadero.Visible = False";
mostCurrent._lblencontrecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 106;BA.debugLine="btnQueMosquito.Visible = False";
mostCurrent._btnquemosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="lblAprenderMosquito.Visible = False";
mostCurrent._lblaprendermosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 108;BA.debugLine="imgEnfermedades.Visible = False";
mostCurrent._imgenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 109;BA.debugLine="lblExplorarMapa2.Visible = False";
mostCurrent._lblexplorarmapa2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 110;BA.debugLine="lblMosquitoAedes.Visible = False";
mostCurrent._lblmosquitoaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 111;BA.debugLine="imgMosquitoIcon.Visible = False";
mostCurrent._imgmosquitoicon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 112;BA.debugLine="imgManos.Visible = False";
mostCurrent._imgmanos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 113;BA.debugLine="btnEnfermedades.Visible = False";
mostCurrent._btnenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 114;BA.debugLine="imgLupa.Visible = False";
mostCurrent._imglupa.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 115;BA.debugLine="btnCerrarAprender.Visible = False";
mostCurrent._btncerraraprender.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="btnVolverAprender.Visible = True";
mostCurrent._btnvolveraprender.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="Dim imgCriadero As ImageView";
_imgcriadero = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 119;BA.debugLine="imgCriadero.Initialize(\"imgCriadero\")";
_imgcriadero.Initialize(mostCurrent.activityBA,"imgCriadero");
 //BA.debugLineNum = 120;BA.debugLine="imgCriadero.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgcriadero.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"CicloMosquito.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="imgCriadero.Gravity = Gravity.FILL";
_imgcriadero.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 122;BA.debugLine="Activity.AddView(imgCriadero, 0, 20%y, 100%x, 60%";
mostCurrent._activity.AddView((android.view.View)(_imgcriadero.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _btnenfermedades_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgcriadero = null;
 //BA.debugLineNum = 130;BA.debugLine="Sub btnEnfermedades_Click";
 //BA.debugLineNum = 132;BA.debugLine="btnQueCriadero.Visible = False";
mostCurrent._btnquecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 133;BA.debugLine="btnCicloAedes.Visible = False";
mostCurrent._btncicloaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="lblEncontreCriadero.Visible = False";
mostCurrent._lblencontrecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 135;BA.debugLine="btnQueMosquito.Visible = False";
mostCurrent._btnquemosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 136;BA.debugLine="lblAprenderMosquito.Visible = False";
mostCurrent._lblaprendermosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 137;BA.debugLine="imgEnfermedades.Visible = False";
mostCurrent._imgenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 138;BA.debugLine="lblExplorarMapa2.Visible = False";
mostCurrent._lblexplorarmapa2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="lblMosquitoAedes.Visible = False";
mostCurrent._lblmosquitoaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 140;BA.debugLine="imgMosquitoIcon.Visible = False";
mostCurrent._imgmosquitoicon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 141;BA.debugLine="imgManos.Visible = False";
mostCurrent._imgmanos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="imgLupa.Visible = False";
mostCurrent._imglupa.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="btnEnfermedades.Visible = False";
mostCurrent._btnenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="btnCerrarAprender.Visible = False";
mostCurrent._btncerraraprender.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="btnVolverAprender.Visible = True";
mostCurrent._btnvolveraprender.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="Dim imgCriadero As ImageView";
_imgcriadero = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 148;BA.debugLine="imgCriadero.Initialize(\"imgCriadero\")";
_imgcriadero.Initialize(mostCurrent.activityBA,"imgCriadero");
 //BA.debugLineNum = 149;BA.debugLine="imgCriadero.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgcriadero.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"lasEnfermedades.png").getObject()));
 //BA.debugLineNum = 150;BA.debugLine="imgCriadero.Gravity = Gravity.FILL";
_imgcriadero.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 151;BA.debugLine="Activity.AddView(imgCriadero, 5%x, 25%y, 90%x, 65";
mostCurrent._activity.AddView((android.view.View)(_imgcriadero.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (65),mostCurrent.activityBA));
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _btnquecriadero_click() throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imgcriadero = null;
 //BA.debugLineNum = 74;BA.debugLine="Sub btnQueCriadero_Click";
 //BA.debugLineNum = 76;BA.debugLine="btnQueCriadero.Visible = False";
mostCurrent._btnquecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 77;BA.debugLine="btnCicloAedes.Visible = False";
mostCurrent._btncicloaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 78;BA.debugLine="lblEncontreCriadero.Visible = False";
mostCurrent._lblencontrecriadero.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 79;BA.debugLine="btnQueMosquito.Visible = False";
mostCurrent._btnquemosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 80;BA.debugLine="lblAprenderMosquito.Visible = False";
mostCurrent._lblaprendermosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 81;BA.debugLine="imgEnfermedades.Visible = False";
mostCurrent._imgenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 82;BA.debugLine="lblExplorarMapa2.Visible = False";
mostCurrent._lblexplorarmapa2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 83;BA.debugLine="lblMosquitoAedes.Visible = False";
mostCurrent._lblmosquitoaedes.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 84;BA.debugLine="imgMosquitoIcon.Visible = False";
mostCurrent._imgmosquitoicon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 85;BA.debugLine="imgManos.Visible = False";
mostCurrent._imgmanos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 86;BA.debugLine="imgLupa.Visible = False";
mostCurrent._imglupa.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="btnEnfermedades.Visible = False";
mostCurrent._btnenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 88;BA.debugLine="btnCerrarAprender.Visible = False";
mostCurrent._btncerraraprender.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 89;BA.debugLine="btnVolverAprender.Visible = True";
mostCurrent._btnvolveraprender.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="Dim imgCriadero As ImageView";
_imgcriadero = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 93;BA.debugLine="imgCriadero.Initialize(\"imgCriadero\")";
_imgcriadero.Initialize(mostCurrent.activityBA,"imgCriadero");
 //BA.debugLineNum = 94;BA.debugLine="imgCriadero.Bitmap = LoadBitmap(File.DirAssets, \"";
_imgcriadero.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"adondeCria.png").getObject()));
 //BA.debugLineNum = 95;BA.debugLine="imgCriadero.Gravity = Gravity.FILL";
_imgcriadero.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 96;BA.debugLine="Activity.AddView(imgCriadero, 0, 20%y, 100%x, 65%";
mostCurrent._activity.AddView((android.view.View)(_imgcriadero.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (65),mostCurrent.activityBA));
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _btnquemosquito_click() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub btnQueMosquito_Click";
 //BA.debugLineNum = 127;BA.debugLine="StartActivity(frmIdentificarMosquito)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv5.getObject()));
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _btnvolveraprender_click() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub btnVolverAprender_Click";
 //BA.debugLineNum = 156;BA.debugLine="Activity.RemoveViewAt(Activity.NumberOfViews - 1)";
mostCurrent._activity.RemoveViewAt((int) (mostCurrent._activity.getNumberOfViews()-1));
 //BA.debugLineNum = 159;BA.debugLine="btnQueCriadero.Visible = True";
mostCurrent._btnquecriadero.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 160;BA.debugLine="btnCicloAedes.Visible = True";
mostCurrent._btncicloaedes.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 161;BA.debugLine="lblEncontreCriadero.Visible = True";
mostCurrent._lblencontrecriadero.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 162;BA.debugLine="btnQueMosquito.Visible = True";
mostCurrent._btnquemosquito.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 163;BA.debugLine="lblAprenderMosquito.Visible = True";
mostCurrent._lblaprendermosquito.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 164;BA.debugLine="imgEnfermedades.Visible = True";
mostCurrent._imgenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 165;BA.debugLine="imgLupa.Visible = True";
mostCurrent._imglupa.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 166;BA.debugLine="lblExplorarMapa2.Visible = True";
mostCurrent._lblexplorarmapa2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 167;BA.debugLine="lblMosquitoAedes.Visible = True";
mostCurrent._lblmosquitoaedes.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 168;BA.debugLine="imgMosquitoIcon.Visible = True";
mostCurrent._imgmosquitoicon.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 169;BA.debugLine="imgManos.Visible = True";
mostCurrent._imgmanos.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 170;BA.debugLine="btnEnfermedades.Visible = True";
mostCurrent._btnenfermedades.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 171;BA.debugLine="btnCerrarAprender.Visible = True";
mostCurrent._btncerraraprender.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 172;BA.debugLine="btnVolverAprender.Visible = False";
mostCurrent._btnvolveraprender.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private imgCirculo As Label";
mostCurrent._imgcirculo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private btnQueCriadero As Button";
mostCurrent._btnquecriadero = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private btnCicloAedes As Button";
mostCurrent._btncicloaedes = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblEncontreCriadero As Label";
mostCurrent._lblencontrecriadero = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private imgLupa As ImageView";
mostCurrent._imglupa = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private btnQueMosquito As Button";
mostCurrent._btnquemosquito = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lblAprenderMosquito As Label";
mostCurrent._lblaprendermosquito = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private imgEnfermedades As ImageView";
mostCurrent._imgenfermedades = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private lblExplorarMapa2 As Button";
mostCurrent._lblexplorarmapa2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lblMosquitoAedes As Label";
mostCurrent._lblmosquitoaedes = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private imgMosquitoIcon As ImageView";
mostCurrent._imgmosquitoicon = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private imgManos As ImageView";
mostCurrent._imgmanos = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private btnEnfermedades As Button";
mostCurrent._btnenfermedades = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim p As Phone";
mostCurrent._vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 40;BA.debugLine="Private btnVolverAprender As Button";
mostCurrent._btnvolveraprender = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btnCerrarAprender As Button";
mostCurrent._btncerraraprender = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim formorigen As String";
_vvvvv7 = "";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
}
