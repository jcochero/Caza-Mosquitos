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

public class frmdatosanteriores extends Activity implements B4AActivity{
	public static frmdatosanteriores mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmdatosanteriores");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmdatosanteriores).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmdatosanteriores");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmdatosanteriores", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmdatosanteriores) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmdatosanteriores) Resume **");
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
		return frmdatosanteriores.class;
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
            BA.LogInfo("** Activity (frmdatosanteriores) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmdatosanteriores) Pause event (activity is not paused). **");
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
            frmdatosanteriores mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmdatosanteriores) Resume **");
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
public static anywheresoftware.b4h.okhttp.OkHttpClientWrapper _vvvvvv1 = null;
public static anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _vvvvvv2 = null;
public static anywheresoftware.b4a.phone.Phone.ContentChooser _vvvvvv3 = null;
public static String _vvvvvvv2 = "";
public static boolean _vvvvvvv3 = false;
public static String _vvvvvvv4 = "";
public static boolean _vvvvvvv5 = false;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv4 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv5 = null;
public static String _vvvvvvv0 = "";
public static String _vvvvvvvv1 = "";
public static String _vvvvvvvv2 = "";
public static String _vvvvvvvv3 = "";
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerrar = null;
public anywheresoftware.b4a.objects.TabStripViewPager _tabstripdatosanteriores = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = "";
public anywheresoftware.b4a.objects.LabelWrapper _lblfecha = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltipo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcalidad = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllng = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkpublico = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkenviado = null;
public anywheresoftware.b4a.objects.LabelWrapper _chkvalidadoexpertos = null;
public anywheresoftware.b4a.objects.LabelWrapper _chkonline = null;
public anywheresoftware.b4a.objects.LabelWrapper _chkenviadobar = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scvfotos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblnombreenvio = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblbarra = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnenviar = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btncambiarpublico = null;
public static boolean _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = false;
public static int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = 0;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar2 = null;
public static int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = 0;
public static boolean _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = false;
public static boolean _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = false;
public anywheresoftware.b4a.objects.Timer _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public static int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = 0;
public anywheresoftware.b4a.objects.LabelWrapper _label9 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label8 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label10 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label12 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label13 = null;
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
 //BA.debugLineNum = 103;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 104;BA.debugLine="Activity.LoadLayout(\"layPerfilDatosAnteriores\")";
mostCurrent._activity.LoadLayout("layPerfilDatosAnteriores",mostCurrent.activityBA);
 //BA.debugLineNum = 107;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 108;BA.debugLine="tabStripDatosAnteriores.LoadLayout(\"layPerfilDat";
mostCurrent._tabstripdatosanteriores.LoadLayout("layPerfilDatosAnteriores_Lista",BA.ObjectToCharSequence("Datos anteriores"));
 //BA.debugLineNum = 109;BA.debugLine="tabStripDatosAnteriores.LoadLayout(\"layPerfilDat";
mostCurrent._tabstripdatosanteriores.LoadLayout("layPerfilDatosAnteriores_Detalle",BA.ObjectToCharSequence("Detalle"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 111;BA.debugLine="tabStripDatosAnteriores.LoadLayout(\"layPerfilDat";
mostCurrent._tabstripdatosanteriores.LoadLayout("layPerfilDatosAnteriores_Lista",BA.ObjectToCharSequence("Previous reports"));
 //BA.debugLineNum = 112;BA.debugLine="tabStripDatosAnteriores.LoadLayout(\"layPerfilDat";
mostCurrent._tabstripdatosanteriores.LoadLayout("layPerfilDatosAnteriores_Detalle",BA.ObjectToCharSequence("Details"));
 };
 //BA.debugLineNum = 117;BA.debugLine="foto1view.Initialize(\"foto1view\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(mostCurrent.activityBA,"foto1view");
 //BA.debugLineNum = 118;BA.debugLine="foto2view.Initialize(\"foto2view\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"foto2view");
 //BA.debugLineNum = 119;BA.debugLine="foto1Borrar.Initialize(\"foto1Borrar\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"foto1Borrar");
 //BA.debugLineNum = 120;BA.debugLine="foto2Borrar.Initialize(\"foto2Borrar\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(mostCurrent.activityBA,"foto2Borrar");
 //BA.debugLineNum = 122;BA.debugLine="scvFotos.Panel.AddView(foto1view, 0dip, 0dip,scvF";
mostCurrent._scvfotos.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (mostCurrent._scvfotos.getWidth()/(double)2),mostCurrent._scvfotos.getHeight());
 //BA.debugLineNum = 123;BA.debugLine="scvFotos.Panel.AddView(foto2view, (scvFotos.Width";
mostCurrent._scvfotos.getPanel().AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) ((mostCurrent._scvfotos.getWidth()/(double)2)*1),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (mostCurrent._scvfotos.getWidth()/(double)2),mostCurrent._scvfotos.getHeight());
 //BA.debugLineNum = 125;BA.debugLine="If hc.IsInitialized = False Then";
if (_vvvvvv1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 126;BA.debugLine="hc.Initialize(\"hc\")";
_vvvvvv1.Initialize("hc");
 };
 //BA.debugLineNum = 129;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 130;BA.debugLine="Up1.B4A_log=True";
_vvvvvv4.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 131;BA.debugLine="Up2.B4A_log=True";
_vvvvvv5.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 132;BA.debugLine="Up1.Initialize(\"Up1\")";
_vvvvvv4.Initialize(processBA,"Up1");
 //BA.debugLineNum = 133;BA.debugLine="Up2.Initialize(\"Up1\")";
_vvvvvv5.Initialize(processBA,"Up1");
 };
 //BA.debugLineNum = 136;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 149;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 150;BA.debugLine="If tabActual = \"Lista\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4).equals("Lista")) { 
 //BA.debugLineNum = 151;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 152;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4).equals("Detalle")) { 
 //BA.debugLineNum = 154;BA.debugLine="tabStripDatosAnteriores.ScrollTo(0,True)";
mostCurrent._tabstripdatosanteriores.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 155;BA.debugLine="tabActual = \"Lista\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = "Lista";
 //BA.debugLineNum = 156;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 158;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 159;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 };
 };
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 146;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 139;BA.debugLine="If nuevalatlng = \"\" And notificacion = False Then";
if ((_vvvvvvv2).equals("") && _vvvvvvv3==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 140;BA.debugLine="ListarDatos";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 };
 //BA.debugLineNum = 142;BA.debugLine="TranslateGUI";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _btncambiarfecha_click() throws Exception{
String _msg = "";
String _fechanueva = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 786;BA.debugLine="Sub btnCambiarFecha_Click";
 //BA.debugLineNum = 787;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 788;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 789;BA.debugLine="msg = Msgbox2(\"Desea cambiar la fecha de este da";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Desea cambiar la fecha de este dato?"),BA.ObjectToCharSequence("Cambiar fecha"),"Si","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 791;BA.debugLine="msg = Msgbox2(\"Do you want to change the date fo";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do you want to change the date for this report?"),BA.ObjectToCharSequence("Change date"),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 794;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 796;BA.debugLine="Dim fechanueva As String";
_fechanueva = "";
 //BA.debugLineNum = 797;BA.debugLine="dated.ShowCalendar = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.ShowCalendar = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 798;BA.debugLine="dated.Year = DateTime.GetYear(DateTime.now)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setYear(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 799;BA.debugLine="dated.Month = DateTime.GetMonth(DateTime.now)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 800;BA.debugLine="dated.DayOfMonth = DateTime.GetDayOfMonth(DateTi";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 801;BA.debugLine="dated.Show(\"Elija la nueva fecha para este dato\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Show("Elija la nueva fecha para este dato","Cambio de fecha","Ok","Cancelar","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 802;BA.debugLine="fechanueva = dated.DayOfMonth & \"-\" & dated.Mont";
_fechanueva = BA.NumberToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getDayOfMonth())+"-"+BA.NumberToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getMonth())+"-"+BA.NumberToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getYear());
 //BA.debugLineNum = 803;BA.debugLine="ToastMessageShow(\"Nueva fecha: \" & fechanueva, F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Nueva fecha: "+_fechanueva),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 805;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 806;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 807;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 808;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","georeferencedDate",(Object)(_fechanueva),_map1);
 //BA.debugLineNum = 810;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("no"),_map1);
 //BA.debugLineNum = 811;BA.debugLine="lblFecha.Text = fechanueva";
mostCurrent._lblfecha.setText(BA.ObjectToCharSequence(_fechanueva));
 //BA.debugLineNum = 812;BA.debugLine="btnEnviar.Visible = True";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 813;BA.debugLine="NewMarcador = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 816;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 818;BA.debugLine="End Sub";
return "";
}
public static String  _btncambiarpublico_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 865;BA.debugLine="Sub btnCambiarPublico_Click";
 //BA.debugLineNum = 867;BA.debugLine="If chkPublico.Checked = True Then";
if (mostCurrent._chkpublico.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 868;BA.debugLine="chkPublico.Checked = False";
mostCurrent._chkpublico.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 869;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 870;BA.debugLine="chkPublico.Text = \"Dato privado\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Dato privado"));
 //BA.debugLineNum = 871;BA.debugLine="btnCambiarPublico.Text = \"Convertir en público\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Convertir en público"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 873;BA.debugLine="chkPublico.Text = \"Private report\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Private report"));
 //BA.debugLineNum = 874;BA.debugLine="btnCambiarPublico.Text = \"Make report public\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Make report public"));
 };
 //BA.debugLineNum = 876;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 877;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 878;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 879;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","privado",(Object)("si"),_map1);
 //BA.debugLineNum = 880;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("no"),_map1);
 //BA.debugLineNum = 881;BA.debugLine="btnEnviar.Visible = True";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 882;BA.debugLine="NewMarcador = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 884;BA.debugLine="chkPublico.Checked = True";
mostCurrent._chkpublico.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 885;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 886;BA.debugLine="chkPublico.Text = \"Dato público\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Dato público"));
 //BA.debugLineNum = 887;BA.debugLine="btnCambiarPublico.Text = \"Convertir en privado\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Convertir en privado"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 889;BA.debugLine="chkPublico.Text = \"Public report\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Public report"));
 //BA.debugLineNum = 890;BA.debugLine="btnCambiarPublico.Text = \"Make report private\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Make report private"));
 };
 //BA.debugLineNum = 892;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 893;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 894;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 895;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","privado",(Object)("no"),_map1);
 //BA.debugLineNum = 896;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("no"),_map1);
 //BA.debugLineNum = 897;BA.debugLine="btnEnviar.Visible = True";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 898;BA.debugLine="NewMarcador = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 900;BA.debugLine="End Sub";
return "";
}
public static String  _btncambiarubicacion_click() throws Exception{
String _msg = "";
 //BA.debugLineNum = 819;BA.debugLine="Sub btnCambiarUbicacion_Click";
 //BA.debugLineNum = 820;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 821;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 822;BA.debugLine="msg = Msgbox2(\"Desea cambiar la ubicación de est";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Desea cambiar la ubicación de este dato?"),BA.ObjectToCharSequence("Cambiar ubicación"),"Si","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 824;BA.debugLine="msg = Msgbox2(\"Do you want to change the locatio";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do you want to change the location for this report?"),BA.ObjectToCharSequence("Change location"),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 827;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 828;BA.debugLine="nuevalatlng = \"\"";
_vvvvvvv2 = "";
 //BA.debugLineNum = 829;BA.debugLine="frmLocalizacion.origen = \"cambio\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvv7._vvvvvvvv5 /*String*/  = "cambio";
 //BA.debugLineNum = 830;BA.debugLine="StartActivity(frmLocalizacion)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv7.getObject()));
 }else {
 //BA.debugLineNum = 833;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 835;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrar_click() throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub btnCerrar_Click";
 //BA.debugLineNum = 217;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 218;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static String  _btnenviar_click() throws Exception{
 //BA.debugLineNum = 919;BA.debugLine="Sub btnEnviar_Click";
 //BA.debugLineNum = 921;BA.debugLine="If Main.modooffline = True Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 922;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 923;BA.debugLine="MsgboxAsync(\"Estas trabajando en modo fuera de";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Estas trabajando en modo fuera de línea. Conectate a internet e inicia sesión para cambiar el estado de los datos"),BA.ObjectToCharSequence("Modo fuera de línea"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 925;BA.debugLine="MsgboxAsync(\"You are working offline. Connect t";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("You are working offline. Connect to the internet and log in to change the reports."),BA.ObjectToCharSequence("Offline mode"),processBA);
 };
 //BA.debugLineNum = 927;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 930;BA.debugLine="Log(\"Chequeando internet\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519333131","Chequeando internet",0);
 //BA.debugLineNum = 931;BA.debugLine="CheckInternet";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 933;BA.debugLine="End Sub";
return "";
}
public static String  _cambiarubicacion() throws Exception{
String _newlat = "";
String _newlng = "";
String[] _arr = null;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 836;BA.debugLine="Sub CambiarUbicacion";
 //BA.debugLineNum = 837;BA.debugLine="Dim newlat As String";
_newlat = "";
 //BA.debugLineNum = 838;BA.debugLine="Dim newlng As String";
_newlng = "";
 //BA.debugLineNum = 840;BA.debugLine="If nuevalatlng = \"\" Then";
if ((_vvvvvvv2).equals("")) { 
 //BA.debugLineNum = 841;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 843;BA.debugLine="Dim arr() As String = Regex.Split(\"_\", nuevalatl";
_arr = anywheresoftware.b4a.keywords.Common.Regex.Split("_",_vvvvvvv2);
 //BA.debugLineNum = 844;BA.debugLine="newlat = arr(0)";
_newlat = _arr[(int) (0)];
 //BA.debugLineNum = 845;BA.debugLine="newlng = arr(1)";
_newlng = _arr[(int) (1)];
 };
 //BA.debugLineNum = 849;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 850;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 851;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 852;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLatitude",(Object)(_newlat),_map1);
 //BA.debugLineNum = 853;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLongitude",(Object)(_newlng),_map1);
 //BA.debugLineNum = 854;BA.debugLine="lblLat.text = newlat";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_newlat));
 //BA.debugLineNum = 855;BA.debugLine="lblLng.text = newlng";
mostCurrent._lbllng.setText(BA.ObjectToCharSequence(_newlng));
 //BA.debugLineNum = 856;BA.debugLine="nuevalatlng = \"\"";
_vvvvvvv2 = "";
 //BA.debugLineNum = 858;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("no"),_map1);
 //BA.debugLineNum = 859;BA.debugLine="btnEnviar.Visible = True";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 860;BA.debugLine="NewMarcador = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 861;BA.debugLine="End Sub";
return "";
}
public static String  _cc_result(boolean _success,String _dir,String _filename) throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
String _fotonombredestino = "";
 //BA.debugLineNum = 750;BA.debugLine="Sub CC_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 751;BA.debugLine="If Success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 754;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 755;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 756;BA.debugLine="Map1.Put(\"Id\", IdProyectoEnviar)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 757;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 758;BA.debugLine="Dim fotoNombreDestino As String";
_fotonombredestino = "";
 //BA.debugLineNum = 760;BA.debugLine="If fotoAdjuntar = \"foto1\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3).equals("foto1")) { 
 //BA.debugLineNum = 761;BA.debugLine="fotoNombreDestino = frmprincipal.fullidcurrentp";
_fotonombredestino = mostCurrent._vvvvvvvvvvvvvvvvvv3._v5 /*String*/ +"_1";
 //BA.debugLineNum = 762;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal &";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_fotonombredestino+".jpg");
 //BA.debugLineNum = 763;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1",(Object)(_fotonombredestino),_map1);
 //BA.debugLineNum = 764;BA.debugLine="foto1view.Bitmap= LoadBitmapSample(File.DirRoot";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_fotonombredestino+".jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3).equals("foto2")) { 
 //BA.debugLineNum = 767;BA.debugLine="fotoNombreDestino = frmprincipal.fullidcurrentp";
_fotonombredestino = mostCurrent._vvvvvvvvvvvvvvvvvv3._v5 /*String*/ +"_2";
 //BA.debugLineNum = 768;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal &";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_fotonombredestino+".jpg");
 //BA.debugLineNum = 769;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2",(Object)(_fotonombredestino),_map1);
 //BA.debugLineNum = 770;BA.debugLine="foto2view.Bitmap= LoadBitmapSample(File.DirRoot";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_fotonombredestino+".jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 };
 };
 //BA.debugLineNum = 774;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 938;BA.debugLine="Sub CheckInternet";
 //BA.debugLineNum = 939;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 940;BA.debugLine="dd.url = Main.serverPath & \"/connect2/connecttest";
_dd.url /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/connecttest.php";
 //BA.debugLineNum = 941;BA.debugLine="dd.EventName = \"TestInternet\"";
_dd.EventName /*String*/  = "TestInternet";
 //BA.debugLineNum = 942;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmdatosanteriores.getObject();
 //BA.debugLineNum = 943;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 944;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4(String _proyectonumero) throws Exception{
String _username = "";
String _dateandtime = "";
String _tipoeval = "";
String _lat = "";
String _lng = "";
String _gpsdetect = "";
String _wifidetect = "";
String _mapadetect = "";
String _valorind1 = "";
String _valorind2 = "";
String _terminado = "";
String _privado = "";
String _estadovalidacion = "";
String _serveridupdate = "";
anywheresoftware.b4a.objects.collections.Map _datosmap = null;
int _conf = 0;
String _dirweb = "";
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 969;BA.debugLine="Sub EnviarDatos(proyectonumero As String)";
 //BA.debugLineNum = 971;BA.debugLine="Dim username,dateandtime,tipoeval,lat,lng,gpsdete";
_username = "";
_dateandtime = "";
_tipoeval = "";
_lat = "";
_lng = "";
_gpsdetect = "";
_wifidetect = "";
_mapadetect = "";
 //BA.debugLineNum = 972;BA.debugLine="Dim valorind1, valorind2, terminado, privado, est";
_valorind1 = "";
_valorind2 = "";
_terminado = "";
_privado = "";
_estadovalidacion = "";
_serveridupdate = "";
 //BA.debugLineNum = 974;BA.debugLine="Dim datosMap As Map";
_datosmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 975;BA.debugLine="datosMap.Initialize";
_datosmap.Initialize();
 //BA.debugLineNum = 976;BA.debugLine="datosMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SEL";
_datosmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local WHERE Id=?",new String[]{_proyectonumero});
 //BA.debugLineNum = 978;BA.debugLine="If datosMap = Null Or datosMap.IsInitialized = Fa";
if (_datosmap== null || _datosmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 979;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 980;BA.debugLine="ToastMessageShow(\"Error cargando el análisis\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error cargando el análisis"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 982;BA.debugLine="ToastMessageShow(\"Error loading the report\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error loading the report"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 984;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 985;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 987;BA.debugLine="username = datosMap.Get(\"usuario\")";
_username = BA.ObjectToString(_datosmap.Get((Object)("usuario")));
 //BA.debugLineNum = 988;BA.debugLine="dateandtime = datosMap.Get(\"georeferenceddate\")";
_dateandtime = BA.ObjectToString(_datosmap.Get((Object)("georeferenceddate")));
 //BA.debugLineNum = 989;BA.debugLine="tipoeval = datosMap.Get(\"tipoeval\")";
_tipoeval = BA.ObjectToString(_datosmap.Get((Object)("tipoeval")));
 //BA.debugLineNum = 990;BA.debugLine="lat = datosMap.Get(\"decimallatitude\")";
_lat = BA.ObjectToString(_datosmap.Get((Object)("decimallatitude")));
 //BA.debugLineNum = 991;BA.debugLine="lng = datosMap.Get(\"decimallongitude\")";
_lng = BA.ObjectToString(_datosmap.Get((Object)("decimallongitude")));
 //BA.debugLineNum = 992;BA.debugLine="gpsdetect = datosMap.Get(\"gpsdetect\")";
_gpsdetect = BA.ObjectToString(_datosmap.Get((Object)("gpsdetect")));
 //BA.debugLineNum = 993;BA.debugLine="wifidetect = datosMap.Get(\"wifidetect\")";
_wifidetect = BA.ObjectToString(_datosmap.Get((Object)("wifidetect")));
 //BA.debugLineNum = 994;BA.debugLine="mapadetect = datosMap.Get(\"mapadetect\")";
_mapadetect = BA.ObjectToString(_datosmap.Get((Object)("mapadetect")));
 //BA.debugLineNum = 995;BA.debugLine="valorind1 = datosMap.Get(\"valororganismo\")";
_valorind1 = BA.ObjectToString(_datosmap.Get((Object)("valororganismo")));
 //BA.debugLineNum = 996;BA.debugLine="valorind2 = datosMap.Get(\"valorambiente\")";
_valorind2 = BA.ObjectToString(_datosmap.Get((Object)("valorambiente")));
 //BA.debugLineNum = 997;BA.debugLine="foto1 = datosMap.Get(\"foto1\")";
_vvvvvvv0 = BA.ObjectToString(_datosmap.Get((Object)("foto1")));
 //BA.debugLineNum = 998;BA.debugLine="foto2 = datosMap.Get(\"foto2\")";
_vvvvvvvv1 = BA.ObjectToString(_datosmap.Get((Object)("foto2")));
 //BA.debugLineNum = 999;BA.debugLine="terminado = datosMap.Get(\"terminado\")";
_terminado = BA.ObjectToString(_datosmap.Get((Object)("terminado")));
 //BA.debugLineNum = 1000;BA.debugLine="privado = datosMap.Get(\"privado\")";
_privado = BA.ObjectToString(_datosmap.Get((Object)("privado")));
 //BA.debugLineNum = 1001;BA.debugLine="estadovalidacion = datosMap.Get(\"estadovalidacio";
_estadovalidacion = BA.ObjectToString(_datosmap.Get((Object)("estadovalidacion")));
 //BA.debugLineNum = 1002;BA.debugLine="If privado = Null Or privado = \"null\" Or privado";
if (_privado== null || (_privado).equals("null") || (_privado).equals("")) { 
 //BA.debugLineNum = 1003;BA.debugLine="privado = \"no\"";
_privado = "no";
 };
 //BA.debugLineNum = 1005;BA.debugLine="estadovalidacion = datosMap.Get(\"estadovalidacio";
_estadovalidacion = BA.ObjectToString(_datosmap.Get((Object)("estadovalidacion")));
 //BA.debugLineNum = 1006;BA.debugLine="If estadovalidacion = \"null\" Then";
if ((_estadovalidacion).equals("null")) { 
 //BA.debugLineNum = 1007;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1008;BA.debugLine="estadovalidacion = \"No Verificado\"";
_estadovalidacion = "No Verificado";
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1010;BA.debugLine="estadovalidacion = \"Unverified\"";
_estadovalidacion = "Unverified";
 };
 };
 //BA.debugLineNum = 1014;BA.debugLine="serverIdupdate = datosMap.Get(\"serverid\")";
_serveridupdate = BA.ObjectToString(_datosmap.Get((Object)("serverid")));
 //BA.debugLineNum = 1015;BA.debugLine="If NewMarcador = False And serverIdupdate = Null";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0==anywheresoftware.b4a.keywords.Common.False && _serveridupdate== null) { 
 //BA.debugLineNum = 1016;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1017;BA.debugLine="ToastMessageShow(\"No se puede actualizar el da";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No se puede actualizar el dato"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1019;BA.debugLine="ToastMessageShow(\"The report cannot be updated";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("The report cannot be updated"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1022;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1024;BA.debugLine="If serverIdupdate <> \"null\" Or serverIdupdate <>";
if ((_serveridupdate).equals("null") == false || (_serveridupdate).equals("") == false || (_serveridupdate).equals("")) { 
 //BA.debugLineNum = 1025;BA.debugLine="NewMarcador = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 1030;BA.debugLine="If terminado <> \"si\" Then";
if ((_terminado).equals("si") == false) { 
 //BA.debugLineNum = 1031;BA.debugLine="Dim conf As Int";
_conf = 0;
 //BA.debugLineNum = 1033;BA.debugLine="Select conf";
switch (BA.switchObjectToInt(_conf,anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE,anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE)) {
case 0: {
 //BA.debugLineNum = 1035;BA.debugLine="Return";
if (true) return "";
 break; }
case 1: {
 //BA.debugLineNum = 1037;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 };
 //BA.debugLineNum = 1041;BA.debugLine="Dim dirWeb As String";
_dirweb = "";
 //BA.debugLineNum = 1042;BA.debugLine="If NewMarcador = True Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1043;BA.debugLine="dirWeb = Main.serverPath & \"/connect2/addpuntoma";
_dirweb = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/addpuntomapa.php";
 }else {
 //BA.debugLineNum = 1045;BA.debugLine="dirWeb = Main.serverPath & \"/connect2/updatepunt";
_dirweb = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/updatepuntomapa.php";
 };
 //BA.debugLineNum = 1048;BA.debugLine="Log(\"Comienza envio de datos\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519529807","Comienza envio de datos",0);
 //BA.debugLineNum = 1051;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 1052;BA.debugLine="dd.url = dirWeb & \"?\" & _ 	\"username=\" & Main.use";
_dd.url /*String*/  = _dirweb+"?"+"username="+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ +"&"+"deviceID="+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv1 /*String*/ +"&"+"dateandtime="+_dateandtime+"&"+"lat="+_lat+"&"+"lng="+_lng+"&"+"valorVinchuca="+_valorind1+"&"+"foto1path="+_vvvvvvv0+"&"+"foto2path="+_vvvvvvvv1+"&"+"foto3path="+_vvvvvvvv2+"&"+"foto4path="+_vvvvvvvv3+"&"+"privado="+_privado+"&"+"gpsdetect="+_gpsdetect+"&"+"wifidetect="+_wifidetect+"&"+"mapadetect="+_mapadetect+"&"+"terminado="+_terminado+"&"+"verificado=No Verificado"+"&"+"serverId="+_serveridupdate;
 //BA.debugLineNum = 1063;BA.debugLine="dd.EventName = \"EnviarDatos\"";
_dd.EventName /*String*/  = "EnviarDatos";
 //BA.debugLineNum = 1064;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmdatosanteriores.getObject();
 //BA.debugLineNum = 1065;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 1066;BA.debugLine="End Sub";
return "";
}
public static String  _enviardatos_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _nd = null;
String _serverid = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 1067;BA.debugLine="Sub EnviarDatos_Complete(Job As HttpJob)";
 //BA.debugLineNum = 1068;BA.debugLine="Log(\"Datos enviados : \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("519595265","Datos enviados : "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 1069;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1070;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 1071;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 1072;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 1073;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1074;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 1075;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 1076;BA.debugLine="Log(act)";
anywheresoftware.b4a.keywords.Common.LogImpl("519595273",_act,0);
 //BA.debugLineNum = 1077;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 1078;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1079;BA.debugLine="ToastMessageShow(\"Error en la carga de marcado";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error en la carga de marcadores"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1081;BA.debugLine="ToastMessageShow(\"Error in loading reports\", T";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error in loading reports"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 1084;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 }else if((_act).equals("Marcadores")) { 
 //BA.debugLineNum = 1086;BA.debugLine="Dim nd As Map";
_nd = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1087;BA.debugLine="nd = parser.NextObject";
_nd = _parser.NextObject();
 //BA.debugLineNum = 1088;BA.debugLine="Dim serverID As String";
_serverid = "";
 //BA.debugLineNum = 1089;BA.debugLine="serverID = nd.Get(\"serverId\")";
_serverid = BA.ObjectToString(_nd.Get((Object)("serverId")));
 //BA.debugLineNum = 1092;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1093;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 1094;BA.debugLine="Map1.Put(\"Id\", IdProyectoEnviar)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 1095;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("si"),_map1);
 //BA.debugLineNum = 1096;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","serverId",(Object)(_serverid),_map1);
 //BA.debugLineNum = 1097;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1098;BA.debugLine="ToastMessageShow(\"Datos enviados, enviando fot";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Datos enviados, enviando fotos"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1100;BA.debugLine="ToastMessageShow(\"Report sent, sending photos\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report sent, sending photos"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1103;BA.debugLine="EnviarFotos";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 }else if((_act).equals("MarcadorActualizado")) { 
 //BA.debugLineNum = 1107;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1108;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 1109;BA.debugLine="Map1.Put(\"Id\", IdProyectoEnviar)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 1110;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("si"),_map1);
 //BA.debugLineNum = 1111;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 };
 //BA.debugLineNum = 1116;BA.debugLine="ToastMessageShow(\"Datos enviados, enviando foto";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Datos enviados, enviando fotos"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1120;BA.debugLine="EnviarFotos";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 //BA.debugLineNum = 1121;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 };
 }else {
 //BA.debugLineNum = 1124;BA.debugLine="Log(\"envio datos not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519595321","envio datos not ok",0);
 //BA.debugLineNum = 1125;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1126;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1128;BA.debugLine="MsgboxAsync(\"There seems to be a problem with o";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with our servers, we will solve it soon!"),BA.ObjectToCharSequence("My bad"),processBA);
 };
 };
 //BA.debugLineNum = 1133;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 1134;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5() throws Exception{
 //BA.debugLineNum = 1141;BA.debugLine="Sub EnviarFotos";
 //BA.debugLineNum = 1145;BA.debugLine="fondoblanco.Initialize(\"fondoblanco\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"fondoblanco");
 //BA.debugLineNum = 1146;BA.debugLine="fondoblanco.Color = Colors.ARGB(190, 255,255,255)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (190),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 1147;BA.debugLine="Activity.AddView(fondoblanco, 0,0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1149;BA.debugLine="If foto1 <> \"null\" Then";
if ((_vvvvvvv0).equals("null") == false) { 
 //BA.debugLineNum = 1150;BA.debugLine="ProgressBar1.Initialize(\"ProgressBar1\")";
mostCurrent._progressbar1.Initialize(mostCurrent.activityBA,"ProgressBar1");
 //BA.debugLineNum = 1151;BA.debugLine="foto1Prg.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1152;BA.debugLine="Activity.AddView(ProgressBar1, 50%x, 30%y, 30%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._progressbar1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1153;BA.debugLine="Activity.AddView(foto1Prg, 15%x, 30%y, 50%x, 20d";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 1154;BA.debugLine="foto1Prg.Text = \"Subiendo Foto 1:\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setText(BA.ObjectToCharSequence("Subiendo Foto 1:"));
 //BA.debugLineNum = 1155;BA.debugLine="foto1Prg.TextColor = Colors.Black";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1156;BA.debugLine="foto1Prg.TextSize = 14";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.setTextSize((float) (14));
 //BA.debugLineNum = 1157;BA.debugLine="totalFotos = totalFotos + 1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7+1);
 };
 //BA.debugLineNum = 1159;BA.debugLine="If foto2 <>  \"null\" Then";
if ((_vvvvvvvv1).equals("null") == false) { 
 //BA.debugLineNum = 1160;BA.debugLine="ProgressBar2.Initialize(\"ProgressBar2\")";
mostCurrent._progressbar2.Initialize(mostCurrent.activityBA,"ProgressBar2");
 //BA.debugLineNum = 1161;BA.debugLine="foto2Prg.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1162;BA.debugLine="Activity.AddView(ProgressBar2, 50%x, 40%y, 30%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._progressbar2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1163;BA.debugLine="Activity.AddView(foto2Prg, 15%x, 40%y, 50%x, 20d";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 1164;BA.debugLine="foto2Prg.Text = \"Subiendo Foto 2:\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setText(BA.ObjectToCharSequence("Subiendo Foto 2:"));
 //BA.debugLineNum = 1165;BA.debugLine="foto2Prg.TextColor = Colors.Black";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1166;BA.debugLine="foto2Prg.TextSize = 14";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setTextSize((float) (14));
 //BA.debugLineNum = 1167;BA.debugLine="totalFotos = totalFotos + 1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7+1);
 };
 //BA.debugLineNum = 1171;BA.debugLine="TimerEnvio.Initialize(\"TimerEnvio\", 1000)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(processBA,"TimerEnvio",(long) (1000));
 //BA.debugLineNum = 1172;BA.debugLine="TimerEnvio.Enabled = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1174;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMosqu";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_vvvvvvv0+".jpg") && _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1175;BA.debugLine="Log(\"Enviando foto 1 \")";
anywheresoftware.b4a.keywords.Common.LogImpl("519660834","Enviando foto 1 ",0);
 //BA.debugLineNum = 1176;BA.debugLine="Up1.doFileUpload(ProgressBar1,Null,File.DirRootE";
_vvvvvv4.doFileUpload(processBA,(android.widget.ProgressBar)(mostCurrent._progressbar1.getObject()),(android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/"+_vvvvvvv0+".jpg",mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/upload_file.php");
 }else {
 //BA.debugLineNum = 1178;BA.debugLine="Log(\"no foto 1\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519660837","no foto 1",0);
 };
 //BA.debugLineNum = 1181;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMosqu";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_vvvvvvvv1+".jpg") && _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1182;BA.debugLine="Log(\"Enviando foto 2 \")";
anywheresoftware.b4a.keywords.Common.LogImpl("519660841","Enviando foto 2 ",0);
 //BA.debugLineNum = 1183;BA.debugLine="Up2.doFileUpload(ProgressBar2,Null,File.DirRootE";
_vvvvvv5.doFileUpload(processBA,(android.widget.ProgressBar)(mostCurrent._progressbar2.getObject()),(android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/"+_vvvvvvvv1+".jpg",mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/upload_file.php");
 }else {
 //BA.debugLineNum = 1185;BA.debugLine="Log(\"no foto 2\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519660844","no foto 2",0);
 };
 //BA.debugLineNum = 1187;BA.debugLine="End Sub";
return "";
}
public static String  _fondoblanco_click() throws Exception{
String _msg = "";
 //BA.debugLineNum = 1189;BA.debugLine="Sub fondoblanco_Click";
 //BA.debugLineNum = 1190;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 1191;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1192;BA.debugLine="msg = Msgbox2(\"Se están enviando las fotografías";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Se están enviando las fotografías, desea cancelar?"),BA.ObjectToCharSequence("Cancelar?"),"Si, cancelar","No!",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1194;BA.debugLine="msg = Msgbox2(\"Photos are being uploaded, do you";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Photos are being uploaded, do you wish to cancel?"),BA.ObjectToCharSequence("Cancel?"),"Yes, cancel","No!",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 1197;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 1198;BA.debugLine="Up1.UploadKill";
_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 1199;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 1200;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 1201;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 1202;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv3.getObject()));
 };
 //BA.debugLineNum = 1204;BA.debugLine="End Sub";
return "";
}
public static String  _fondogris_click() throws Exception{
 //BA.debugLineNum = 694;BA.debugLine="Sub fondogris_Click";
 //BA.debugLineNum = 695;BA.debugLine="fotogrande.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 //BA.debugLineNum = 696;BA.debugLine="foto1Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 697;BA.debugLine="foto2Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.RemoveView();
 //BA.debugLineNum = 698;BA.debugLine="fondogris.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.RemoveView();
 //BA.debugLineNum = 699;BA.debugLine="End Sub";
return "";
}
public static String  _foto1borrar_click() throws Exception{
String _msg = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 707;BA.debugLine="Sub foto1Borrar_Click";
 //BA.debugLineNum = 708;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 709;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 710;BA.debugLine="msg = Msgbox2(\"Seguro que desea quitar esta foto";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Seguro que desea quitar esta foto de este registro?"),BA.ObjectToCharSequence("Quitar foto"),"Si","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 712;BA.debugLine="msg = Msgbox2(\"Sure that you want to remove this";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Sure that you want to remove this photo from this report?"),BA.ObjectToCharSequence("Remove photo"),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 714;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 716;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 717;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 718;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 719;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1",anywheresoftware.b4a.keywords.Common.Null,_map1);
 //BA.debugLineNum = 720;BA.debugLine="foto1view.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 721;BA.debugLine="foto1Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 722;BA.debugLine="fotogrande.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 }else {
 //BA.debugLineNum = 724;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 727;BA.debugLine="End Sub";
return "";
}
public static String  _foto1view_click() throws Exception{
 //BA.debugLineNum = 644;BA.debugLine="Sub foto1view_Click";
 //BA.debugLineNum = 647;BA.debugLine="If foto1view.Bitmap = Null Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getBitmap()== null) { 
 //BA.debugLineNum = 648;BA.debugLine="fotoAdjuntar = \"foto1\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "foto1";
 //BA.debugLineNum = 649;BA.debugLine="CC.Initialize(\"CC\")";
_vvvvvv3.Initialize("CC");
 //BA.debugLineNum = 650;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 651;BA.debugLine="CC.Show(\"image/\", \"Elija la foto\")";
_vvvvvv3.Show(processBA,"image/","Elija la foto");
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 653;BA.debugLine="CC.Show(\"image/\", \"Choose the photo\")";
_vvvvvv3.Show(processBA,"image/","Choose the photo");
 };
 }else {
 //BA.debugLineNum = 657;BA.debugLine="fondogris.Initialize(\"fondogris\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"fondogris");
 //BA.debugLineNum = 658;BA.debugLine="fondogris.Color = Colors.ARGB(150,0,0,0)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 659;BA.debugLine="Activity.AddView(fondogris, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 661;BA.debugLine="fotogrande.Initialize(\"fotogrande\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"fotogrande");
 //BA.debugLineNum = 662;BA.debugLine="fotogrande.Bitmap = foto1view.Bitmap";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getBitmap());
 //BA.debugLineNum = 663;BA.debugLine="Log(foto1view.Width)";
anywheresoftware.b4a.keywords.Common.LogImpl("518612243",BA.NumberToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getWidth()),0);
 //BA.debugLineNum = 664;BA.debugLine="Activity.AddView(fotogrande, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 666;BA.debugLine="Activity.AddView(foto1Borrar, 10dip, 10dip, 40di";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 };
 //BA.debugLineNum = 669;BA.debugLine="End Sub";
return "";
}
public static String  _foto2borrar_click() throws Exception{
String _msg = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 728;BA.debugLine="Sub foto2Borrar_Click";
 //BA.debugLineNum = 729;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 730;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 731;BA.debugLine="msg = Msgbox2(\"Seguro que desea quitar esta foto";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Seguro que desea quitar esta foto de este registro?"),BA.ObjectToCharSequence("Quitar foto"),"Si","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 733;BA.debugLine="msg = Msgbox2(\"Sure that you want to remove this";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Sure that you want to remove this photo from this report?"),BA.ObjectToCharSequence("Remove photo"),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 735;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 737;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 738;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 739;BA.debugLine="Map1.Put(\"Id\", currentDatoId)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7));
 //BA.debugLineNum = 740;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2",anywheresoftware.b4a.keywords.Common.Null,_map1);
 //BA.debugLineNum = 741;BA.debugLine="foto2view.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 742;BA.debugLine="foto2Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.RemoveView();
 //BA.debugLineNum = 743;BA.debugLine="fotogrande.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 }else {
 //BA.debugLineNum = 745;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 748;BA.debugLine="End Sub";
return "";
}
public static String  _foto2view_click() throws Exception{
 //BA.debugLineNum = 670;BA.debugLine="Sub foto2view_Click";
 //BA.debugLineNum = 671;BA.debugLine="If foto2view.Bitmap = Null Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getBitmap()== null) { 
 //BA.debugLineNum = 672;BA.debugLine="fotoAdjuntar = \"foto2\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "foto2";
 //BA.debugLineNum = 673;BA.debugLine="CC.Initialize(\"CC\")";
_vvvvvv3.Initialize("CC");
 //BA.debugLineNum = 674;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 675;BA.debugLine="CC.Show(\"image/\", \"Elija la foto\")";
_vvvvvv3.Show(processBA,"image/","Elija la foto");
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 677;BA.debugLine="CC.Show(\"image/\", \"Choose the photo\")";
_vvvvvv3.Show(processBA,"image/","Choose the photo");
 };
 }else {
 //BA.debugLineNum = 682;BA.debugLine="fondogris.Initialize(\"fondogris\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"fondogris");
 //BA.debugLineNum = 683;BA.debugLine="fondogris.Color = Colors.ARGB(150,0,0,0)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 684;BA.debugLine="Activity.AddView(fondogris, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 686;BA.debugLine="fotogrande.Initialize(\"fotogrande\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"fotogrande");
 //BA.debugLineNum = 687;BA.debugLine="fotogrande.Bitmap = foto2view.Bitmap";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setBitmap(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getBitmap());
 //BA.debugLineNum = 688;BA.debugLine="Activity.AddView(fotogrande, 0, 0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 690;BA.debugLine="Activity.AddView(foto2Borrar, 10dip, 10dip, 40di";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 };
 //BA.debugLineNum = 692;BA.debugLine="End Sub";
return "";
}
public static String  _fotogrande_click() throws Exception{
 //BA.debugLineNum = 700;BA.debugLine="Sub fotogrande_Click";
 //BA.debugLineNum = 701;BA.debugLine="fotogrande.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 //BA.debugLineNum = 702;BA.debugLine="foto1Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 703;BA.debugLine="foto2Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.RemoveView();
 //BA.debugLineNum = 704;BA.debugLine="fondogris.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.RemoveView();
 //BA.debugLineNum = 705;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 32;BA.debugLine="Dim ListView1 As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private btnCerrar As Button";
mostCurrent._btncerrar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private tabStripDatosAnteriores As TabStrip";
mostCurrent._tabstripdatosanteriores = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 38;BA.debugLine="Dim tabActual As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = "";
 //BA.debugLineNum = 41;BA.debugLine="Private lblFecha As Label";
mostCurrent._lblfecha = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private lblTipo As Label";
mostCurrent._lbltipo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lblLat As Label";
mostCurrent._lbllat = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblCalidad As Label";
mostCurrent._lblcalidad = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private lblLng As Label";
mostCurrent._lbllng = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private chkPublico As CheckBox";
mostCurrent._chkpublico = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private chkEnviado As CheckBox";
mostCurrent._chkenviado = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private chkValidadoExpertos As Label";
mostCurrent._chkvalidadoexpertos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private chkOnline As Label";
mostCurrent._chkonline = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private chkEnviadoBar As Label";
mostCurrent._chkenviadobar = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private scvFotos As HorizontalScrollView";
mostCurrent._scvfotos = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lblNombreEnvio As Label";
mostCurrent._lblnombreenvio = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private lblBarra As Label";
mostCurrent._lblbarra = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Dim currentDatoId As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim foto1view As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Dim foto2view As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Dim fotogrande As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Dim fondogris As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Dim foto1Borrar As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Dim foto2Borrar As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Dim fotoAdjuntar As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 66;BA.debugLine="Private btnEnviar As Button";
mostCurrent._btnenviar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Dim IdProyectoEnviar As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = "";
 //BA.debugLineNum = 68;BA.debugLine="Private btnCambiarPublico As Button";
mostCurrent._btncambiarpublico = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim NewMarcador As Boolean = True";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 70;BA.debugLine="Dim numberTasks As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = 0;
 //BA.debugLineNum = 72;BA.debugLine="Dim dated As DateDialog";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.DateDialog();
 //BA.debugLineNum = 76;BA.debugLine="Private fondoblanco As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private foto1Prg As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private foto2Prg As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private ProgressBar2 As ProgressBar";
mostCurrent._progressbar2 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private totalFotos As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = 0;
 //BA.debugLineNum = 82;BA.debugLine="Private foto1Sent As Boolean";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = false;
 //BA.debugLineNum = 83;BA.debugLine="Private foto2Sent As Boolean";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = false;
 //BA.debugLineNum = 84;BA.debugLine="Private TimerEnvio As Timer";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 85;BA.debugLine="Dim fotosEnviadas As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = 0;
 //BA.debugLineNum = 87;BA.debugLine="Private Label9 As Label";
mostCurrent._label9 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private Label6 As Label";
mostCurrent._label6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private Label7 As Label";
mostCurrent._label7 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private Label8 As Label";
mostCurrent._label8 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private Label10 As Label";
mostCurrent._label10 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private Label12 As Label";
mostCurrent._label12 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private Label3 As Label";
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private Label13 As Label";
mostCurrent._label13 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5() throws Exception{
anywheresoftware.b4a.objects.collections.List _listmaps = null;
int _r = 0;
anywheresoftware.b4a.objects.collections.Map _rmap = null;
String _esent = "";
String _sppresente = "";
String _latdato = "";
String _lngdato = "";
String _fechadato = "";
String _iddato = "";
 //BA.debugLineNum = 227;BA.debugLine="Sub ListarDatos";
 //BA.debugLineNum = 228;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 229;BA.debugLine="ListView1.Color = Colors.White";
mostCurrent._listview1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 230;BA.debugLine="ListView1.TwoLinesAndBitmap.Label.Width = ListVie";
mostCurrent._listview1.getTwoLinesAndBitmap().Label.setWidth((int) (mostCurrent._listview1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 231;BA.debugLine="ListView1.TwoLinesAndBitmap.SecondLabel.Width = L";
mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel.setWidth((int) (mostCurrent._listview1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 232;BA.debugLine="ListView1.TwoLinesAndBitmap.Label.TextColor = Col";
mostCurrent._listview1.getTwoLinesAndBitmap().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 233;BA.debugLine="ListView1.TwoLinesAndBitmap.SecondLabel.TextColor";
mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 234;BA.debugLine="ListView1.TwoLinesAndBitmap.Label.TextSize = 14";
mostCurrent._listview1.getTwoLinesAndBitmap().Label.setTextSize((float) (14));
 //BA.debugLineNum = 235;BA.debugLine="ListView1.TwoLinesAndBitmap.SecondLabel.TextSize";
mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel.setTextSize((float) (12));
 //BA.debugLineNum = 236;BA.debugLine="ListView1.TwoLinesAndBitmap.Label.left = 20dip";
mostCurrent._listview1.getTwoLinesAndBitmap().Label.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 237;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Left = List";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setLeft((int) (mostCurrent._listview1.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 238;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Gravity = G";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 239;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Width = 20d";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 240;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Height = 20";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 241;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.top = (List";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setTop((int) ((mostCurrent._listview1.getTwoLinesAndBitmap().getItemHeight()/(double)2)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 246;BA.debugLine="Dim listmaps As List";
_listmaps = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 247;BA.debugLine="listmaps.Initialize";
_listmaps.Initialize();
 //BA.debugLineNum = 248;BA.debugLine="listmaps = DBUtils.ExecuteListOfMaps(Starter.sqlD";
_listmaps = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv2 /*anywheresoftware.b4a.objects.collections.List*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local WHERE usuario=?",new String[]{mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ },(int) (0));
 //BA.debugLineNum = 250;BA.debugLine="If listmaps = Null Or listmaps.IsInitialized = Fa";
if (_listmaps== null || _listmaps.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 251;BA.debugLine="hayAnteriores = False";
_vvvvvvv5 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 252;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 253;BA.debugLine="ToastMessageShow(\"No tienes datos anteriores...";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No tienes datos anteriores..."),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 255;BA.debugLine="ToastMessageShow(\"There are no previous reports";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("There are no previous reports..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 257;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 258;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 259;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 261;BA.debugLine="If listmaps.Size = 0 Then";
if (_listmaps.getSize()==0) { 
 //BA.debugLineNum = 263;BA.debugLine="hayAnteriores = False";
_vvvvvvv5 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 264;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 265;BA.debugLine="ToastMessageShow(\"No tienes datos anteriores..";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No tienes datos anteriores..."),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 267;BA.debugLine="ToastMessageShow(\"There are no previous report";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("There are no previous reports..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 269;BA.debugLine="tabStripDatosAnteriores.ScrollTo(0,True)";
mostCurrent._tabstripdatosanteriores.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 270;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 271;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 274;BA.debugLine="hayAnteriores = True";
_vvvvvvv5 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 275;BA.debugLine="Try";
try { //BA.debugLineNum = 276;BA.debugLine="For r = 0 To listmaps.Size -1";
{
final int step42 = 1;
final int limit42 = (int) (_listmaps.getSize()-1);
_r = (int) (0) ;
for (;_r <= limit42 ;_r = _r + step42 ) {
 //BA.debugLineNum = 277;BA.debugLine="Dim rmap As Map";
_rmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 278;BA.debugLine="rmap = listmaps.Get(r)";
_rmap = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(_listmaps.Get(_r)));
 //BA.debugLineNum = 279;BA.debugLine="Dim esent As String = rmap.Get(\"evalsent\")";
_esent = BA.ObjectToString(_rmap.Get((Object)("evalsent")));
 //BA.debugLineNum = 280;BA.debugLine="Dim sppresente As String = rmap.Get(\"valororg";
_sppresente = BA.ObjectToString(_rmap.Get((Object)("valororganismo")));
 //BA.debugLineNum = 281;BA.debugLine="Dim latdato As String = rmap.Get(\"decimallati";
_latdato = BA.ObjectToString(_rmap.Get((Object)("decimallatitude")));
 //BA.debugLineNum = 282;BA.debugLine="Dim lngdato As String = rmap.Get(\"decimallong";
_lngdato = BA.ObjectToString(_rmap.Get((Object)("decimallongitude")));
 //BA.debugLineNum = 283;BA.debugLine="Dim fechadato As String = rmap.Get(\"georefere";
_fechadato = BA.ObjectToString(_rmap.Get((Object)("georeferenceddate")));
 //BA.debugLineNum = 284;BA.debugLine="Dim iddato As String = rmap.Get(\"id\")";
_iddato = BA.ObjectToString(_rmap.Get((Object)("id")));
 //BA.debugLineNum = 285;BA.debugLine="If esent = \"si\" Then";
if ((_esent).equals("si")) { 
 //BA.debugLineNum = 288;BA.debugLine="If sppresente = \"infestans\" Then";
if ((_sppresente).equals("infestans")) { 
 //BA.debugLineNum = 289;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"Fecha: \" &";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Fecha: "+_fechadato),BA.ObjectToCharSequence(_latdato+"/"+_lngdato),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"warning.png").getObject()),(Object)(_iddato));
 }else if(_sppresente== null) { 
 //BA.debugLineNum = 291;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"Fecha: \" &";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Fecha: "+_fechadato),BA.ObjectToCharSequence(_latdato+"/"+_lngdato),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"indet.png").getObject()),(Object)(_iddato));
 }else if((_sppresente).equals("null")) { 
 //BA.debugLineNum = 293;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"Fecha: \" &";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Fecha: "+_fechadato),BA.ObjectToCharSequence(_latdato+"/"+_lngdato),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"check.png").getObject()),(Object)(_iddato));
 };
 }else {
 //BA.debugLineNum = 297;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"Fecha: \" &";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Fecha: "+_fechadato),BA.ObjectToCharSequence(_latdato+"/"+_lngdato),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"not_sent.png").getObject()),(Object)(_iddato));
 };
 }
};
 } 
       catch (Exception e64) {
			processBA.setLastException(e64); //BA.debugLineNum = 301;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 302;BA.debugLine="ToastMessageShow(\"Ha habido un error cargando";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Ha habido un error cargando sus datos anteriores, intente de nuevo!"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 304;BA.debugLine="ToastMessageShow(\"There's been an error, plea";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("There's been an error, please try again!"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 306;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 307;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 };
 };
 //BA.debugLineNum = 379;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 381;BA.debugLine="Sub ListView1_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 383;BA.debugLine="currentDatoId = Value";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = BA.ObjectToString(_value);
 //BA.debugLineNum = 384;BA.debugLine="VerDetalles(currentDatoId, False)";
_verdetalles(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim hc As OkHttpClient";
_vvvvvv1 = new anywheresoftware.b4h.okhttp.OkHttpClientWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Dim out As OutputStream";
_vvvvvv2 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Dim CC As ContentChooser";
_vvvvvv3 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 12;BA.debugLine="Dim nuevalatlng As String";
_vvvvvvv2 = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim notificacion As Boolean";
_vvvvvvv3 = false;
 //BA.debugLineNum = 16;BA.debugLine="Dim serverIdNum As String";
_vvvvvvv4 = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim hayAnteriores As Boolean";
_vvvvvvv5 = false;
 //BA.debugLineNum = 21;BA.debugLine="Dim Up1 As UploadFilePhp";
_vvvvvv4 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 22;BA.debugLine="Dim Up2 As UploadFilePhp";
_vvvvvv5 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 23;BA.debugLine="Private foto1 As String";
_vvvvvvv0 = "";
 //BA.debugLineNum = 24;BA.debugLine="Private foto2 As String";
_vvvvvvvv1 = "";
 //BA.debugLineNum = 25;BA.debugLine="Private foto3 As String";
_vvvvvvvv2 = "";
 //BA.debugLineNum = 26;BA.debugLine="Private foto4 As String";
_vvvvvvvv3 = "";
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 1315;BA.debugLine="Sub ResetMessages";
 //BA.debugLineNum = 1318;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 1319;BA.debugLine="dd.url = Main.serverPath & \"/connect2/resetmessag";
_dd.url /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/resetmessages_new.php?deviceID="+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv1 /*String*/ ;
 //BA.debugLineNum = 1320;BA.debugLine="dd.EventName = \"ResetMessages\"";
_dd.EventName /*String*/  = "ResetMessages";
 //BA.debugLineNum = 1321;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmdatosanteriores.getObject();
 //BA.debugLineNum = 1322;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 1324;BA.debugLine="End Sub";
return "";
}
public static String  _resetmessages_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
 //BA.debugLineNum = 1325;BA.debugLine="Sub ResetMessages_Complete(Job As HttpJob)";
 //BA.debugLineNum = 1326;BA.debugLine="Log(\"Reset messages: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("520316161","Reset messages: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 1327;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1328;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 1329;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 1330;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 1331;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1332;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 1333;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 1334;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 1335;BA.debugLine="ToastMessageShow(\"Error\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("Error reseteando los mensajes, ")) { 
 //BA.debugLineNum = 1337;BA.debugLine="ToastMessageShow(\"Login failed\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Login failed"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("ResetMessages")) { 
 };
 }else {
 //BA.debugLineNum = 1342;BA.debugLine="Log(\"reset messages not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("520316177","reset messages not ok",0);
 //BA.debugLineNum = 1343;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1344;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1346;BA.debugLine="MsgboxAsync(\"There seems to be a problem with o";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with our servers, we will solve it soon!"),BA.ObjectToCharSequence("My bad"),processBA);
 };
 };
 //BA.debugLineNum = 1350;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 1351;BA.debugLine="End Sub";
return "";
}
public static String  _tabstripdatosanteriores_pageselected(int _position) throws Exception{
 //BA.debugLineNum = 197;BA.debugLine="Sub tabStripDatosAnteriores_PageSelected (Position";
 //BA.debugLineNum = 198;BA.debugLine="If Position = 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 199;BA.debugLine="foto1Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 200;BA.debugLine="foto2Borrar.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.RemoveView();
 //BA.debugLineNum = 201;BA.debugLine="ListarDatos";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 //BA.debugLineNum = 202;BA.debugLine="NewMarcador = True";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = anywheresoftware.b4a.keywords.Common.True;
 }else if(_position==1) { 
 //BA.debugLineNum = 204;BA.debugLine="If hayAnteriores = False Then";
if (_vvvvvvv5==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 205;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 206;BA.debugLine="ToastMessageShow(\"No tienes datos anteriores..";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No tienes datos anteriores..."),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 208;BA.debugLine="ToastMessageShow(\"There are no previous report";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("There are no previous reports..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 210;BA.debugLine="tabStripDatosAnteriores.ScrollTo(0,True)";
mostCurrent._tabstripdatosanteriores.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _testinternet_complete(caza.mosquito.httpjob _job) throws Exception{
 //BA.debugLineNum = 945;BA.debugLine="Sub TestInternet_Complete(Job As HttpJob)";
 //BA.debugLineNum = 946;BA.debugLine="Log(\"Chequeo de internet: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("519464193","Chequeo de internet: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 947;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 949;BA.debugLine="Main.modooffline = False";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 950;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 952;BA.debugLine="EnviarDatos(IdProyectoEnviar)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2);
 }else {
 //BA.debugLineNum = 955;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 956;BA.debugLine="MsgboxAsync(\"No hay conexión a internet, prueba";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No hay conexión a internet, prueba cuando estés conectado!"),BA.ObjectToCharSequence("No hay internet"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 958;BA.debugLine="MsgboxAsync(\"There is no internet, try again wh";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There is no internet, try again when you're connected!"),BA.ObjectToCharSequence("No internet"),processBA);
 };
 //BA.debugLineNum = 960;BA.debugLine="Main.modooffline = True";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 961;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 962;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 963;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 964;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 };
 //BA.debugLineNum = 966;BA.debugLine="End Sub";
return "";
}
public static String  _timerenvio_tick() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 1207;BA.debugLine="Sub TimerEnvio_Tick";
 //BA.debugLineNum = 1208;BA.debugLine="If fotosEnviadas = totalFotos Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7==_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7) { 
 //BA.debugLineNum = 1209;BA.debugLine="Log(\"TODAS LAS FOTOS FUERON ENVIADAS CORRECTAMEN";
anywheresoftware.b4a.keywords.Common.LogImpl("519791874","TODAS LAS FOTOS FUERON ENVIADAS CORRECTAMENTE",0);
 //BA.debugLineNum = 1210;BA.debugLine="Up1.UploadKill";
_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 1211;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 1212;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 };
 //BA.debugLineNum = 1217;BA.debugLine="ToastMessageShow(\"Evaluación enviada\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Evaluación enviada"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1219;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1220;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 1221;BA.debugLine="Map1.Put(\"Id\", IdProyectoEnviar)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 1222;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1sent",(Object)("si"),_map1);
 //BA.debugLineNum = 1223;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2sent",(Object)("si"),_map1);
 //BA.debugLineNum = 1224;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1225;BA.debugLine="ToastMessageShow(\"Evaluación enviada\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Evaluación enviada"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1227;BA.debugLine="ToastMessageShow(\"Report sent\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report sent"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1230;BA.debugLine="TimerEnvio.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1231;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 1232;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 1235;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub TranslateGUI";
 //BA.debugLineNum = 171;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 172;BA.debugLine="Label6.Text = \" Fecha:\"";
mostCurrent._label6.setText(BA.ObjectToCharSequence(" Fecha:"));
 //BA.debugLineNum = 173;BA.debugLine="Label7.Text = \" Especie (usuario):\"";
mostCurrent._label7.setText(BA.ObjectToCharSequence(" Especie (usuario):"));
 //BA.debugLineNum = 174;BA.debugLine="Label9.Text = \" Especie (según revisor)\"";
mostCurrent._label9.setText(BA.ObjectToCharSequence(" Especie (según revisor)"));
 //BA.debugLineNum = 175;BA.debugLine="Label8.Text = \" Ubicación:\"";
mostCurrent._label8.setText(BA.ObjectToCharSequence(" Ubicación:"));
 //BA.debugLineNum = 176;BA.debugLine="Label12.Text = \" Enviado:\"";
mostCurrent._label12.setText(BA.ObjectToCharSequence(" Enviado:"));
 //BA.debugLineNum = 177;BA.debugLine="Label3.Text = \"Enviado\"";
mostCurrent._label3.setText(BA.ObjectToCharSequence("Enviado"));
 //BA.debugLineNum = 178;BA.debugLine="Label2.Text = \"En línea\"";
mostCurrent._label2.setText(BA.ObjectToCharSequence("En línea"));
 //BA.debugLineNum = 179;BA.debugLine="Label13.Text = \"Verificado\"";
mostCurrent._label13.setText(BA.ObjectToCharSequence("Verificado"));
 //BA.debugLineNum = 180;BA.debugLine="btnCambiarPublico.Text = \"Hacer dato privado\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Hacer dato privado"));
 //BA.debugLineNum = 181;BA.debugLine="btnEnviar.Text = \" Enviar\"";
mostCurrent._btnenviar.setText(BA.ObjectToCharSequence(" Enviar"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 183;BA.debugLine="Label6.Text = \" Date:\"";
mostCurrent._label6.setText(BA.ObjectToCharSequence(" Date:"));
 //BA.debugLineNum = 184;BA.debugLine="Label7.Text = \" Species (user):\"";
mostCurrent._label7.setText(BA.ObjectToCharSequence(" Species (user):"));
 //BA.debugLineNum = 185;BA.debugLine="Label9.Text = \" Species (reviewer)\"";
mostCurrent._label9.setText(BA.ObjectToCharSequence(" Species (reviewer)"));
 //BA.debugLineNum = 186;BA.debugLine="Label8.Text = \" Location:\"";
mostCurrent._label8.setText(BA.ObjectToCharSequence(" Location:"));
 //BA.debugLineNum = 187;BA.debugLine="Label12.Text = \" Sent:\"";
mostCurrent._label12.setText(BA.ObjectToCharSequence(" Sent:"));
 //BA.debugLineNum = 188;BA.debugLine="Label3.Text = \"Sent\"";
mostCurrent._label3.setText(BA.ObjectToCharSequence("Sent"));
 //BA.debugLineNum = 189;BA.debugLine="Label2.Text = \"Online\"";
mostCurrent._label2.setText(BA.ObjectToCharSequence("Online"));
 //BA.debugLineNum = 190;BA.debugLine="Label13.Text = \"Verified\"";
mostCurrent._label13.setText(BA.ObjectToCharSequence("Verified"));
 //BA.debugLineNum = 191;BA.debugLine="btnEnviar.Text = \" Upload\"";
mostCurrent._btnenviar.setText(BA.ObjectToCharSequence(" Upload"));
 //BA.debugLineNum = 192;BA.debugLine="btnCambiarPublico.Text = \"Make report private\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Make report private"));
 };
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public static String  _up1_sendfile(String _value) throws Exception{
 //BA.debugLineNum = 1241;BA.debugLine="Sub Up1_sendFile (value As String)";
 //BA.debugLineNum = 1242;BA.debugLine="Log(\"sendfile event:\" & value)";
anywheresoftware.b4a.keywords.Common.LogImpl("519922945","sendfile event:"+_value,0);
 //BA.debugLineNum = 1243;BA.debugLine="If value = \"success\" Then";
if ((_value).equals("success")) { 
 //BA.debugLineNum = 1245;BA.debugLine="If foto1 <> \"null\" And ProgressBar1.IsInitialize";
if ((_vvvvvvv0).equals("null") == false && mostCurrent._progressbar1.IsInitialized() && mostCurrent._progressbar1.getProgress()==100) { 
 //BA.debugLineNum = 1246;BA.debugLine="Log(\"TERMINO EL ENVIO FOTO 1\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519922949","TERMINO EL ENVIO FOTO 1",0);
 //BA.debugLineNum = 1247;BA.debugLine="foto1Sent = True";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1248;BA.debugLine="fotosEnviadas = fotosEnviadas+ 1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7+1);
 };
 //BA.debugLineNum = 1251;BA.debugLine="Log(\"FOTO #\" & fotosEnviadas & \"/\" & totalFotos";
anywheresoftware.b4a.keywords.Common.LogImpl("519922954","FOTO #"+BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7)+"/"+BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7)+" ENVIADA",0);
 }else if((_value).equals("Error!")) { 
 //BA.debugLineNum = 1254;BA.debugLine="Log(\"FOTO error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("519922957","FOTO error",0);
 //BA.debugLineNum = 1255;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1256;BA.debugLine="MsgboxAsync(\"Ha habido un error en el envío. Re";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ha habido un error en el envío. Revisa tu conexión a Internet e intenta de nuevo desde 'Datos Anteriores'"),BA.ObjectToCharSequence("Oops!"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1258;BA.debugLine="MsgboxAsync(\"There has been an error during the";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There has been an error during the upload. Check your internet connection and try again"),BA.ObjectToCharSequence("Oops!"),processBA);
 }_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 1260;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 1261;BA.debugLine="TimerEnvio.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1262;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 1263;BA.debugLine="Activity_Create(False)";
_activity_create(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1266;BA.debugLine="End Sub";
return "";
}
public static String  _up1_statusupload(String _value) throws Exception{
 //BA.debugLineNum = 1238;BA.debugLine="Sub Up1_statusUpload (value As String)";
 //BA.debugLineNum = 1239;BA.debugLine="End Sub";
return "";
}
public static String  _up1_statusvisible(boolean _onoff,String _value) throws Exception{
 //BA.debugLineNum = 1267;BA.debugLine="Sub Up1_statusVISIBLE (onoff As Boolean,value As S";
 //BA.debugLineNum = 1269;BA.debugLine="End Sub";
return "";
}
public static String  _up2_sendfile(String _value) throws Exception{
 //BA.debugLineNum = 1275;BA.debugLine="Sub Up2_sendFile (value As String)";
 //BA.debugLineNum = 1276;BA.debugLine="Log(\"sendfile event:\" & value)";
anywheresoftware.b4a.keywords.Common.LogImpl("520119553","sendfile event:"+_value,0);
 //BA.debugLineNum = 1277;BA.debugLine="If value = \"success\" Then";
if ((_value).equals("success")) { 
 //BA.debugLineNum = 1279;BA.debugLine="If foto2 <> \"null\" And ProgressBar2.IsInitialize";
if ((_vvvvvvvv1).equals("null") == false && mostCurrent._progressbar2.IsInitialized() && mostCurrent._progressbar2.getProgress()==100) { 
 //BA.debugLineNum = 1280;BA.debugLine="Log(\"TERMINO EL ENVIO FOTO 2\")";
anywheresoftware.b4a.keywords.Common.LogImpl("520119557","TERMINO EL ENVIO FOTO 2",0);
 //BA.debugLineNum = 1281;BA.debugLine="foto2Sent = True";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 1282;BA.debugLine="fotosEnviadas = fotosEnviadas+ 1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7+1);
 };
 //BA.debugLineNum = 1285;BA.debugLine="Log(\"FOTO #\" & fotosEnviadas & \"/\" & totalFotos";
anywheresoftware.b4a.keywords.Common.LogImpl("520119562","FOTO #"+BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7)+"/"+BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7)+" ENVIADA",0);
 }else if((_value).equals("Error!")) { 
 //BA.debugLineNum = 1287;BA.debugLine="Log(\"FOTO error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("520119564","FOTO error",0);
 //BA.debugLineNum = 1288;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 1289;BA.debugLine="MsgboxAsync(\"Ha habido un error en el envío. Re";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ha habido un error en el envío. Revisa tu conexión a Internet e intenta de nuevo desde 'Datos Anteriores'"),BA.ObjectToCharSequence("Oops!"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 1291;BA.debugLine="MsgboxAsync(\"There has been an error during the";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There has been an error during the upload. Check your internet connection and try again"),BA.ObjectToCharSequence("Oops!"),processBA);
 };
 //BA.debugLineNum = 1294;BA.debugLine="Up1.UploadKill";
_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 1295;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 1296;BA.debugLine="TimerEnvio.Enabled = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1297;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 1298;BA.debugLine="Activity_Create(False)";
_activity_create(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 1300;BA.debugLine="End Sub";
return "";
}
public static String  _up2_statusupload(String _value) throws Exception{
 //BA.debugLineNum = 1272;BA.debugLine="Sub Up2_statusUpload (value As String)";
 //BA.debugLineNum = 1273;BA.debugLine="End Sub";
return "";
}
public static String  _up2_statusvisible(boolean _onoff,String _value) throws Exception{
 //BA.debugLineNum = 1301;BA.debugLine="Sub Up2_statusVISIBLE (onoff As Boolean,value As S";
 //BA.debugLineNum = 1303;BA.debugLine="End Sub";
return "";
}
public static String  _verdetalles(String _datoid,boolean _serverid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _datomap = null;
String _datoenviado = "";
String _spname = "";
String _datorevisado = "";
String _datoprivado = "";
String _foto1path = "";
String _foto2path = "";
String _foto4path = "";
anywheresoftware.b4a.objects.drawable.ColorDrawable _cd = null;
anywheresoftware.b4a.objects.LabelWrapper _cdplus1 = null;
anywheresoftware.b4a.objects.LabelWrapper _cdplus2 = null;
 //BA.debugLineNum = 388;BA.debugLine="Sub VerDetalles(datoID As String, serverId As Bool";
 //BA.debugLineNum = 390;BA.debugLine="Dim datoMap As Map";
_datomap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 391;BA.debugLine="datoMap.Initialize";
_datomap.Initialize();
 //BA.debugLineNum = 392;BA.debugLine="If serverId = False Then";
if (_serverid==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 393;BA.debugLine="datoMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SEL";
_datomap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local WHERE Id=?",new String[]{_datoid});
 }else {
 //BA.debugLineNum = 395;BA.debugLine="datoMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SEL";
_datomap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local WHERE serverid=?",new String[]{_datoid});
 //BA.debugLineNum = 396;BA.debugLine="hayAnteriores = True";
_vvvvvvv5 = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 400;BA.debugLine="Dim datoenviado As String";
_datoenviado = "";
 //BA.debugLineNum = 401;BA.debugLine="datoenviado = datoMap.Get(\"evalsent\")";
_datoenviado = BA.ObjectToString(_datomap.Get((Object)("evalsent")));
 //BA.debugLineNum = 402;BA.debugLine="If datoenviado = \"si\" Then";
if ((_datoenviado).equals("si")) { 
 //BA.debugLineNum = 403;BA.debugLine="chkEnviado.Visible = True";
mostCurrent._chkenviado.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 404;BA.debugLine="btnEnviar.Visible = False";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 405;BA.debugLine="chkEnviado.Checked = True";
mostCurrent._chkenviado.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 406;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 407;BA.debugLine="chkEnviado.Text = \"Enviado\"";
mostCurrent._chkenviado.setText(BA.ObjectToCharSequence("Enviado"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 409;BA.debugLine="chkEnviado.Text = \"Sent\"";
mostCurrent._chkenviado.setText(BA.ObjectToCharSequence("Sent"));
 };
 //BA.debugLineNum = 411;BA.debugLine="lblBarra.Visible = True";
mostCurrent._lblbarra.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 412;BA.debugLine="lblBarra.Width = chkOnline.Left + chkOnline.Widt";
mostCurrent._lblbarra.setWidth((int) (mostCurrent._chkonline.getLeft()+mostCurrent._chkonline.getWidth()-mostCurrent._chkenviadobar.getLeft()));
 //BA.debugLineNum = 413;BA.debugLine="Label9.Color = Colors.ARGB(255,247,150,71)";
mostCurrent._label9.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (247),(int) (150),(int) (71)));
 }else {
 //BA.debugLineNum = 415;BA.debugLine="Label9.Color = Colors.ARGB(0,247,150,71)";
mostCurrent._label9.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (247),(int) (150),(int) (71)));
 //BA.debugLineNum = 416;BA.debugLine="lblBarra.Visible = False";
mostCurrent._lblbarra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 417;BA.debugLine="btnEnviar.Visible = True";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 418;BA.debugLine="chkEnviado.Visible = False";
mostCurrent._chkenviado.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 421;BA.debugLine="If datoMap = Null Or datoMap.IsInitialized = Fals";
if (_datomap== null || _datomap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 422;BA.debugLine="tabStripDatosAnteriores.ScrollTo(0,True)";
mostCurrent._tabstripdatosanteriores.ScrollTo((int) (0),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 423;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 424;BA.debugLine="ToastMessageShow(\"Dato no encontrado...\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Dato no encontrado..."),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 426;BA.debugLine="ToastMessageShow(\"Report not found...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report not found..."),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 430;BA.debugLine="IdProyectoEnviar = datoID";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = _datoid;
 //BA.debugLineNum = 432;BA.debugLine="lblNombreEnvio.Text = datoMap.Get(\"georeferenced";
mostCurrent._lblnombreenvio.setText(BA.ObjectToCharSequence(_datomap.Get((Object)("georeferenceddate"))));
 //BA.debugLineNum = 433;BA.debugLine="lblFecha.Text = datoMap.Get(\"georeferenceddate\")";
mostCurrent._lblfecha.setText(BA.ObjectToCharSequence(_datomap.Get((Object)("georeferenceddate"))));
 //BA.debugLineNum = 434;BA.debugLine="If datoMap.Get(\"par2\") = Null Then";
if (_datomap.Get((Object)("par2"))== null) { 
 //BA.debugLineNum = 435;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 436;BA.debugLine="lblTipo.Text = \"Indeterminado\"";
mostCurrent._lbltipo.setText(BA.ObjectToCharSequence("Indeterminado"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 438;BA.debugLine="lblTipo.Text = \"Undetermined\"";
mostCurrent._lbltipo.setText(BA.ObjectToCharSequence("Undetermined"));
 };
 }else {
 //BA.debugLineNum = 442;BA.debugLine="Dim spname As String";
_spname = "";
 //BA.debugLineNum = 443;BA.debugLine="If datoMap.Get(\"par2\") = \"1\" Then";
if ((_datomap.Get((Object)("par2"))).equals((Object)("1"))) { 
 //BA.debugLineNum = 444;BA.debugLine="spname = \"Triatoma infestans\"";
_spname = "Triatoma infestans";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("2"))) { 
 //BA.debugLineNum = 446;BA.debugLine="spname = \"Triatoma guasayana\"";
_spname = "Triatoma guasayana";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("3"))) { 
 //BA.debugLineNum = 448;BA.debugLine="spname = \"Triatoma sordida\"";
_spname = "Triatoma sordida";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("4"))) { 
 //BA.debugLineNum = 450;BA.debugLine="spname = \"Triatoma garciabesi\"";
_spname = "Triatoma garciabesi";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("5"))) { 
 //BA.debugLineNum = 452;BA.debugLine="spname = \"Triatoma patagónica\"";
_spname = "Triatoma patagónica";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("6"))) { 
 //BA.debugLineNum = 454;BA.debugLine="spname = \"Triatoma delpontei\"";
_spname = "Triatoma delpontei";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("7"))) { 
 //BA.debugLineNum = 456;BA.debugLine="spname = \"Triatoma platensis\"";
_spname = "Triatoma platensis";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("8"))) { 
 //BA.debugLineNum = 458;BA.debugLine="spname = \"Triatoma rubrovaria\"";
_spname = "Triatoma rubrovaria";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("9"))) { 
 //BA.debugLineNum = 460;BA.debugLine="spname = \"Triatoma eratyrusiformis\"";
_spname = "Triatoma eratyrusiformis";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("10"))) { 
 //BA.debugLineNum = 462;BA.debugLine="spname = \"Triatoma breyeri\"";
_spname = "Triatoma breyeri";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("11"))) { 
 //BA.debugLineNum = 464;BA.debugLine="spname = \"Panstrongylus rufotuberculatus\"";
_spname = "Panstrongylus rufotuberculatus";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("12"))) { 
 //BA.debugLineNum = 466;BA.debugLine="spname = \"Triatoma limai\"";
_spname = "Triatoma limai";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("13"))) { 
 //BA.debugLineNum = 468;BA.debugLine="spname = \"Psammolestes coreodes\"";
_spname = "Psammolestes coreodes";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("14"))) { 
 //BA.debugLineNum = 470;BA.debugLine="spname = \"Panstrongylus geniculatus\"";
_spname = "Panstrongylus geniculatus";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("15"))) { 
 //BA.debugLineNum = 472;BA.debugLine="spname = \"Panstrongylus megistus\"";
_spname = "Panstrongylus megistus";
 }else if((_datomap.Get((Object)("par2"))).equals((Object)("16"))) { 
 //BA.debugLineNum = 474;BA.debugLine="spname = \"Panstrongylus guentheri\"";
_spname = "Panstrongylus guentheri";
 };
 //BA.debugLineNum = 476;BA.debugLine="lblTipo.Text = spname";
mostCurrent._lbltipo.setText(BA.ObjectToCharSequence(_spname));
 };
 //BA.debugLineNum = 483;BA.debugLine="Dim spname As String";
_spname = "";
 //BA.debugLineNum = 484;BA.debugLine="If datoMap.Get(\"valororganismo\") = Null Then";
if (_datomap.Get((Object)("valororganismo"))== null) { 
 //BA.debugLineNum = 485;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 486;BA.debugLine="spname = \"Indeterminado\"";
_spname = "Indeterminado";
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 488;BA.debugLine="spname = \"Undetermined\"";
_spname = "Undetermined";
 };
 }else {
 //BA.debugLineNum = 491;BA.debugLine="If datoMap.Get(\"valororganismo\") = \"infestans\"";
if ((_datomap.Get((Object)("valororganismo"))).equals((Object)("infestans"))) { 
 //BA.debugLineNum = 492;BA.debugLine="spname = \"Triatoma infestans\"";
_spname = "Triatoma infestans";
 //BA.debugLineNum = 493;BA.debugLine="Label9.Color = Colors.ARGB(255,226,0,0)";
mostCurrent._label9.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (226),(int) (0),(int) (0)));
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("guasayana"))) { 
 //BA.debugLineNum = 495;BA.debugLine="spname = \"Triatoma guasayana\"";
_spname = "Triatoma guasayana";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("sordida"))) { 
 //BA.debugLineNum = 497;BA.debugLine="spname = \"Triatoma sordida\"";
_spname = "Triatoma sordida";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("garciabesi"))) { 
 //BA.debugLineNum = 499;BA.debugLine="spname = \"Triatoma garciabesi\"";
_spname = "Triatoma garciabesi";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("patagónica"))) { 
 //BA.debugLineNum = 501;BA.debugLine="spname = \"Triatoma patagónica\"";
_spname = "Triatoma patagónica";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("delpontei"))) { 
 //BA.debugLineNum = 503;BA.debugLine="spname = \"Triatoma delpontei\"";
_spname = "Triatoma delpontei";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("platensis"))) { 
 //BA.debugLineNum = 505;BA.debugLine="spname = \"Triatoma platensis\"";
_spname = "Triatoma platensis";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("rubrovaria"))) { 
 //BA.debugLineNum = 507;BA.debugLine="spname = \"Triatoma rubrovaria\"";
_spname = "Triatoma rubrovaria";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("eratyrusiformis"))) { 
 //BA.debugLineNum = 509;BA.debugLine="spname = \"Triatoma eratyrusiformis\"";
_spname = "Triatoma eratyrusiformis";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("breyeri"))) { 
 //BA.debugLineNum = 511;BA.debugLine="spname = \"Triatoma breyeri\"";
_spname = "Triatoma breyeri";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("Panstrongylus_rufotuberculatus"))) { 
 //BA.debugLineNum = 513;BA.debugLine="spname = \"Panstrongylus rufotuberculatus\"";
_spname = "Panstrongylus rufotuberculatus";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("limai"))) { 
 //BA.debugLineNum = 515;BA.debugLine="spname = \"Triatoma limai\"";
_spname = "Triatoma limai";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("Psammolestes_coreodes"))) { 
 //BA.debugLineNum = 517;BA.debugLine="spname = \"Psammolestes coreodes\"";
_spname = "Psammolestes coreodes";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("Panstrongylus_geniculatus"))) { 
 //BA.debugLineNum = 519;BA.debugLine="spname = \"Panstrongylus geniculatus\"";
_spname = "Panstrongylus geniculatus";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("Panstrongylus_megistus"))) { 
 //BA.debugLineNum = 521;BA.debugLine="spname = \"Panstrongylus megistus\"";
_spname = "Panstrongylus megistus";
 }else if((_datomap.Get((Object)("valororganismo"))).equals((Object)("Panstrongylus_guentheri"))) { 
 //BA.debugLineNum = 523;BA.debugLine="spname = \"Panstrongylus guentheri\"";
_spname = "Panstrongylus guentheri";
 }else {
 //BA.debugLineNum = 525;BA.debugLine="spname = datoMap.Get(\"valororganismo\")";
_spname = BA.ObjectToString(_datomap.Get((Object)("valororganismo")));
 };
 };
 //BA.debugLineNum = 528;BA.debugLine="lblCalidad.Text = spname";
mostCurrent._lblcalidad.setText(BA.ObjectToCharSequence(_spname));
 //BA.debugLineNum = 530;BA.debugLine="lblLat.Text = datoMap.Get(\"decimallatitude\")";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_datomap.Get((Object)("decimallatitude"))));
 //BA.debugLineNum = 531;BA.debugLine="lblLng.Text = datoMap.Get(\"decimallongitude\")";
mostCurrent._lbllng.setText(BA.ObjectToCharSequence(_datomap.Get((Object)("decimallongitude"))));
 //BA.debugLineNum = 536;BA.debugLine="Dim datorevisado As String";
_datorevisado = "";
 //BA.debugLineNum = 537;BA.debugLine="datorevisado = datoMap.Get(\"estadovalidacion\")";
_datorevisado = BA.ObjectToString(_datomap.Get((Object)("estadovalidacion")));
 //BA.debugLineNum = 538;BA.debugLine="If datorevisado = \"validado\" Then";
if ((_datorevisado).equals("validado")) { 
 //BA.debugLineNum = 539;BA.debugLine="chkEnviado.Visible = True";
mostCurrent._chkenviado.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 540;BA.debugLine="btnEnviar.Visible = False";
mostCurrent._btnenviar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 541;BA.debugLine="chkEnviado.Checked = True";
mostCurrent._chkenviado.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 542;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 543;BA.debugLine="chkEnviado.Text = \"Enviado\"";
mostCurrent._chkenviado.setText(BA.ObjectToCharSequence("Enviado"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 545;BA.debugLine="chkEnviado.Text = \"Sent\"";
mostCurrent._chkenviado.setText(BA.ObjectToCharSequence("Sent"));
 };
 //BA.debugLineNum = 547;BA.debugLine="lblBarra.Visible = True";
mostCurrent._lblbarra.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 548;BA.debugLine="lblBarra.Width = chkValidadoExpertos.Left + chk";
mostCurrent._lblbarra.setWidth((int) (mostCurrent._chkvalidadoexpertos.getLeft()+mostCurrent._chkvalidadoexpertos.getWidth()-mostCurrent._chkenviadobar.getLeft()));
 };
 //BA.debugLineNum = 553;BA.debugLine="Dim datoprivado As String";
_datoprivado = "";
 //BA.debugLineNum = 554;BA.debugLine="datoprivado = datoMap.Get(\"privado\")";
_datoprivado = BA.ObjectToString(_datomap.Get((Object)("privado")));
 //BA.debugLineNum = 555;BA.debugLine="If datoprivado = \"no\" Or datoprivado = \"\" Or dat";
if ((_datoprivado).equals("no") || (_datoprivado).equals("") || (_datoprivado).equals("null")) { 
 //BA.debugLineNum = 556;BA.debugLine="chkPublico.Checked = True";
mostCurrent._chkpublico.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 557;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 558;BA.debugLine="chkPublico.Text = \"Dato público\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Dato público"));
 //BA.debugLineNum = 559;BA.debugLine="btnCambiarPublico.Text = \"Hacer dato privado\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Hacer dato privado"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 561;BA.debugLine="chkPublico.Text = \"Public report\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Public report"));
 //BA.debugLineNum = 562;BA.debugLine="btnCambiarPublico.Text = \"Make report private\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Make report private"));
 };
 }else {
 //BA.debugLineNum = 565;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 566;BA.debugLine="chkPublico.Text = \"Dato privado\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Dato privado"));
 //BA.debugLineNum = 567;BA.debugLine="btnCambiarPublico.Text = \"Hacer dato público\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Hacer dato público"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 569;BA.debugLine="chkPublico.Text = \"Private report\"";
mostCurrent._chkpublico.setText(BA.ObjectToCharSequence("Private report"));
 //BA.debugLineNum = 570;BA.debugLine="btnCambiarPublico.Text = \"Make report public\"";
mostCurrent._btncambiarpublico.setText(BA.ObjectToCharSequence("Make report public"));
 };
 //BA.debugLineNum = 573;BA.debugLine="chkPublico.Checked = False";
mostCurrent._chkpublico.setChecked(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 579;BA.debugLine="foto1view.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 580;BA.debugLine="foto2view.Bitmap = Null";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 581;BA.debugLine="Dim foto1path As String";
_foto1path = "";
 //BA.debugLineNum = 582;BA.debugLine="Dim foto2path As String";
_foto2path = "";
 //BA.debugLineNum = 583;BA.debugLine="Dim foto4path As String";
_foto4path = "";
 //BA.debugLineNum = 584;BA.debugLine="foto1path = datoMap.Get(\"foto1\") & \".jpg\"";
_foto1path = BA.ObjectToString(_datomap.Get((Object)("foto1")))+".jpg";
 //BA.debugLineNum = 585;BA.debugLine="foto2path = datoMap.Get(\"foto2\") & \".jpg\"";
_foto2path = BA.ObjectToString(_datomap.Get((Object)("foto2")))+".jpg";
 //BA.debugLineNum = 586;BA.debugLine="foto4path = datoMap.Get(\"foto4\") & \".jpg\"";
_foto4path = BA.ObjectToString(_datomap.Get((Object)("foto4")))+".jpg";
 //BA.debugLineNum = 587;BA.debugLine="scvFotos.Panel.Invalidate";
mostCurrent._scvfotos.getPanel().Invalidate();
 //BA.debugLineNum = 590;BA.debugLine="foto1Borrar.Color = Colors.ARGB(150,255,255,255)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 591;BA.debugLine="foto2Borrar.Color = Colors.ARGB(150,255,255,255)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 592;BA.debugLine="foto1Borrar.Text = \"X Eliminar imagen\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setText(BA.ObjectToCharSequence("X Eliminar imagen"));
 //BA.debugLineNum = 593;BA.debugLine="foto2Borrar.Text = \"X Eliminar imagen\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setText(BA.ObjectToCharSequence("X Eliminar imagen"));
 //BA.debugLineNum = 594;BA.debugLine="foto1Borrar.TextColor = Colors.Black";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 595;BA.debugLine="foto2Borrar.TextColor = Colors.Black";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 598;BA.debugLine="Dim cd As ColorDrawable";
_cd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 599;BA.debugLine="cd.Initialize2(Colors.Transparent,10dip,2dip,Col";
_cd.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 602;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMosq";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_foto1path)) { 
 //BA.debugLineNum = 603;BA.debugLine="foto1view.Bitmap= LoadBitmapSample(File.DirRoot";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_foto1path,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else {
 //BA.debugLineNum = 605;BA.debugLine="foto1view.Background=cd";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.setBackground((android.graphics.drawable.Drawable)(_cd.getObject()));
 //BA.debugLineNum = 606;BA.debugLine="Dim cdplus1 As Label";
_cdplus1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 607;BA.debugLine="cdplus1.Initialize(\"\")";
_cdplus1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 608;BA.debugLine="cdplus1.Text = \"+\"";
_cdplus1.setText(BA.ObjectToCharSequence("+"));
 //BA.debugLineNum = 609;BA.debugLine="cdplus1.TextSize = 48";
_cdplus1.setTextSize((float) (48));
 //BA.debugLineNum = 610;BA.debugLine="cdplus1.TextColor = Colors.LightGray";
_cdplus1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 611;BA.debugLine="cdplus1.Gravity = Bit.Or(Gravity.CENTER_VERTIC";
_cdplus1.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 612;BA.debugLine="scvFotos.Panel.AddView(cdplus1, foto1view.Left,";
mostCurrent._scvfotos.getPanel().AddView((android.view.View)(_cdplus1.getObject()),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getLeft(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getTop(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getWidth(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getHeight());
 };
 //BA.debugLineNum = 614;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMosq";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_foto2path)) { 
 //BA.debugLineNum = 615;BA.debugLine="foto2view.Bitmap= LoadBitmapSample(File.DirRoot";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",_foto2path,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1005),mostCurrent.activityBA)).getObject()));
 }else {
 //BA.debugLineNum = 617;BA.debugLine="foto2view.Background=cd";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setBackground((android.graphics.drawable.Drawable)(_cd.getObject()));
 //BA.debugLineNum = 618;BA.debugLine="Dim cdplus2 As Label";
_cdplus2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 619;BA.debugLine="cdplus2.Initialize(\"\")";
_cdplus2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 620;BA.debugLine="cdplus2.Text = \"+\"";
_cdplus2.setText(BA.ObjectToCharSequence("+"));
 //BA.debugLineNum = 621;BA.debugLine="cdplus2.TextSize = 48";
_cdplus2.setTextSize((float) (48));
 //BA.debugLineNum = 622;BA.debugLine="cdplus2.TextColor = Colors.LightGray";
_cdplus2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 623;BA.debugLine="cdplus2.Gravity = Bit.Or(Gravity.CENTER_VERTIC";
_cdplus2.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 624;BA.debugLine="scvFotos.Panel.AddView(cdplus2, foto2view.Left";
mostCurrent._scvfotos.getPanel().AddView((android.view.View)(_cdplus2.getObject()),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLeft(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getTop(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getWidth(),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getHeight());
 };
 };
 //BA.debugLineNum = 629;BA.debugLine="tabStripDatosAnteriores.ScrollTo(2,True)";
mostCurrent._tabstripdatosanteriores.ScrollTo((int) (2),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 630;BA.debugLine="tabActual = \"Detalle\"";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = "Detalle";
 //BA.debugLineNum = 631;BA.debugLine="notificacion = False";
_vvvvvvv3 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 632;BA.debugLine="serverId  = False";
_serverid = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 633;BA.debugLine="ResetMessages";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 634;BA.debugLine="End Sub";
return "";
}
}
