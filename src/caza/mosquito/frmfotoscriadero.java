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

public class frmfotoscriadero extends Activity implements B4AActivity{
	public static frmfotoscriadero mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmfotoscriadero");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmfotoscriadero).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmfotoscriadero");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmfotoscriadero", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmfotoscriadero) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmfotoscriadero) Resume **");
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
		return frmfotoscriadero.class;
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
            BA.LogInfo("** Activity (frmfotoscriadero) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmfotoscriadero) Pause event (activity is not paused). **");
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
            frmfotoscriadero mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmfotoscriadero) Resume **");
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
public static com.spinter.uploadfilephp.UploadFilePhp _up1 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _up2 = null;
public static String _currentfoto = "";
public anywheresoftware.b4a.objects.ImageViewWrapper _foto_ventral = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _foto_dorsal = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncontinuar_parte3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnfoto1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnfoto2 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblenviar = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfondotext = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtaccion = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _radnoaccion = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper _radsiaccion = null;
public static String _foto1 = "";
public static String _foto2 = "";
public static String _foto3 = "";
public static String _foto4 = "";
public static int _totalfotos = 0;
public static boolean _foto1sent = false;
public static boolean _foto2sent = false;
public static int _fotosenviadas = 0;
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _pw = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public static boolean _hizoalgo = false;
public static boolean _foto_dorsal_existe = false;
public static boolean _foto_ventral_existe = false;
public anywheresoftware.b4a.objects.LabelWrapper _lblinstrucciones = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblreportepublico = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkprivado = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblstatus = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso3 = null;
public caza.mosquito.main _main = null;
public caza.mosquito.frmcamara _frmcamara = null;
public caza.mosquito.frmquehacer _frmquehacer = null;
public caza.mosquito.register _register = null;
public caza.mosquito.frmlogin _frmlogin = null;
public caza.mosquito.frmprincipal _frmprincipal = null;
public caza.mosquito.starter _starter = null;
public caza.mosquito.dbutils _dbutils = null;
public caza.mosquito.downloadservice _downloadservice = null;
public caza.mosquito.firebasemessaging _firebasemessaging = null;
public caza.mosquito.frmabout _frmabout = null;
public caza.mosquito.frmaprender _frmaprender = null;
public caza.mosquito.frmcomofotos _frmcomofotos = null;
public caza.mosquito.frmcomotransmiten _frmcomotransmiten = null;
public caza.mosquito.frmcomovemos_app _frmcomovemos_app = null;
public caza.mosquito.frmcomovemos_enfermedades _frmcomovemos_enfermedades = null;
public caza.mosquito.frmcomovemos_porqueexiste _frmcomovemos_porqueexiste = null;
public caza.mosquito.frmdatosanteriores _frmdatosanteriores = null;
public caza.mosquito.frmdondecria _frmdondecria = null;
public caza.mosquito.frmeditprofile _frmeditprofile = null;
public caza.mosquito.frmelmosquito _frmelmosquito = null;
public caza.mosquito.frmenfermedades _frmenfermedades = null;
public caza.mosquito.frmfotos _frmfotos = null;
public caza.mosquito.frmidentificarmosquito _frmidentificarmosquito = null;
public caza.mosquito.frminfografias_main _frminfografias_main = null;
public caza.mosquito.frminstrucciones _frminstrucciones = null;
public caza.mosquito.frmlocalizacion _frmlocalizacion = null;
public caza.mosquito.frmmapa _frmmapa = null;
public caza.mosquito.frmpoliticadatos _frmpoliticadatos = null;
public caza.mosquito.httputils2service _httputils2service = null;
public caza.mosquito.multipartpost _multipartpost = null;
public caza.mosquito.uploadfiles _uploadfiles = null;
public caza.mosquito.utilidades _utilidades = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="Activity.LoadLayout(\"layReporteCriadero\")";
mostCurrent._activity.LoadLayout("layReporteCriadero",mostCurrent.activityBA);
 //BA.debugLineNum = 57;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 58;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 60;BA.debugLine="btnFoto1.Enabled = True";
mostCurrent._btnfoto1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 61;BA.debugLine="btnFoto1.Visible = True";
mostCurrent._btnfoto1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 62;BA.debugLine="Foto_Dorsal.Visible = True";
mostCurrent._foto_dorsal.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 75;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 76;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 77;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 78;BA.debugLine="If Msgbox2(\"Volver al inicio? Esto cancelará el";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Volver al inicio? Esto cancelará el envío actual"),BA.ObjectToCharSequence("SALIR"),"Si","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 79;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 80;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 81;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 82;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 84;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 87;BA.debugLine="If Msgbox2(\"Back to the start? This will cancel";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Back to the start? This will cancel the current upload"),BA.ObjectToCharSequence("EXIT"),"Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 88;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 89;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 90;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 91;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 93;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 }else {
 //BA.debugLineNum = 97;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 98;BA.debugLine="If Msgbox2(\"Volver al inicio?\", \"SALIR\", \"Si\",";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Volver al inicio?"),BA.ObjectToCharSequence("SALIR"),"Si","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 99;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 100;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 101;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 102;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 104;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 107;BA.debugLine="If Msgbox2(\"Back to the start?\", \"EXIT\", \"Yes\",";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Back to the start?"),BA.ObjectToCharSequence("EXIT"),"Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 108;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 109;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 110;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 111;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 113;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 71;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 72;BA.debugLine="pw.ReleaseKeepAlive";
mostCurrent._pw.ReleaseKeepAlive();
 };
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 66;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 68;BA.debugLine="PreviewFotos";
_previewfotos();
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinuar_parte1_click() throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub btnContinuar_Parte1_Click";
 //BA.debugLineNum = 171;BA.debugLine="If foto_dorsal_existe = False Then";
if (_foto_dorsal_existe==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 172;BA.debugLine="ToastMessageShow(\"Recuerde sacar la foto antes d";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Recuerde sacar la foto antes de seguir"),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 174;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 175;BA.debugLine="Activity.LoadLayout(\"layReporteCriadero_Parte2\")";
mostCurrent._activity.LoadLayout("layReporteCriadero_Parte2",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinuar_parte2_click() throws Exception{
 //BA.debugLineNum = 178;BA.debugLine="Sub btnContinuar_Parte2_Click";
 //BA.debugLineNum = 179;BA.debugLine="If radSiAccion.Checked = True Then";
if (mostCurrent._radsiaccion.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 180;BA.debugLine="hizoAlgo = True";
_hizoalgo = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 182;BA.debugLine="If txtAccion.Text = \"\" Then";
if ((mostCurrent._txtaccion.getText()).equals("")) { 
 //BA.debugLineNum = 183;BA.debugLine="ToastMessageShow(\"Si realizó alguna acción para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Si realizó alguna acción para eliminar el criadero, contala brevemente!"),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 185;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 186;BA.debugLine="Activity.LoadLayout(\"layReporteCriadero_Parte3\"";
mostCurrent._activity.LoadLayout("layReporteCriadero_Parte3",mostCurrent.activityBA);
 //BA.debugLineNum = 187;BA.debugLine="foto3 = txtAccion.Text";
mostCurrent._foto3 = mostCurrent._txtaccion.getText();
 //BA.debugLineNum = 188;BA.debugLine="btnFoto2.Enabled = True";
mostCurrent._btnfoto2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="btnFoto2.Visible = True";
mostCurrent._btnfoto2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 190;BA.debugLine="Foto_Ventral.Visible = True";
mostCurrent._foto_ventral.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 191;BA.debugLine="chkPrivado.Visible = True";
mostCurrent._chkprivado.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 192;BA.debugLine="lblReportePublico.Visible = True";
mostCurrent._lblreportepublico.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 195;BA.debugLine="hizoAlgo = False";
_hizoalgo = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 197;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 198;BA.debugLine="Activity.LoadLayout(\"layReporteCriadero_Parte3\")";
mostCurrent._activity.LoadLayout("layReporteCriadero_Parte3",mostCurrent.activityBA);
 //BA.debugLineNum = 200;BA.debugLine="btnFoto2.Enabled = False";
mostCurrent._btnfoto2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 201;BA.debugLine="btnFoto2.Visible = False";
mostCurrent._btnfoto2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 202;BA.debugLine="Foto_Ventral.Visible = False";
mostCurrent._foto_ventral.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="chkPrivado.Visible = True";
mostCurrent._chkprivado.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 204;BA.debugLine="lblReportePublico.Visible = True";
mostCurrent._lblreportepublico.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 205;BA.debugLine="lblEnviar.Top = 50%y";
mostCurrent._lblenviar.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 206;BA.debugLine="btnContinuar_Parte3.Top = 50%y";
mostCurrent._btncontinuar_parte3.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 207;BA.debugLine="lblPaso3.Text = \"Envía las fotos!\"";
mostCurrent._lblpaso3.setText(BA.ObjectToCharSequence("Envía las fotos!"));
 };
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinuar_parte3_click() throws Exception{
String _msg = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 223;BA.debugLine="Sub btnContinuar_Parte3_Click";
 //BA.debugLineNum = 226;BA.debugLine="If btnContinuar_Parte3.Text = \"\" Then";
if ((mostCurrent._btncontinuar_parte3.getText()).equals("")) { 
 //BA.debugLineNum = 227;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 228;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 229;BA.debugLine="msg = Msgbox2(\"Se están enviando las fotografía";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Se están enviando las fotografías, desea cancelar?"),BA.ObjectToCharSequence("Cancelar?"),"Si, cancelar","No!","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 231;BA.debugLine="msg = Msgbox2(\"Photos are being uploaded, do yo";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Photos are being uploaded, do you want to cancel?"),BA.ObjectToCharSequence("Cancel?"),"Yes, cancel","No!","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 233;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 234;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 235;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 //BA.debugLineNum = 236;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 237;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 238;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmprincipal.getObject()));
 //BA.debugLineNum = 239;BA.debugLine="Return";
if (true) return "";
 };
 };
 //BA.debugLineNum = 255;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 256;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 257;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 258;BA.debugLine="If chkPrivado.Checked = True Then";
if (mostCurrent._chkprivado.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 259;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","privado",(Object)("no"),_map1);
 }else {
 //BA.debugLineNum = 261;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","privado",(Object)("si"),_map1);
 };
 //BA.debugLineNum = 263;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto3",(Object)(mostCurrent._foto3),_map1);
 //BA.debugLineNum = 264;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto4",(Object)(mostCurrent._main._fotopath3 /*String*/ ),_map1);
 //BA.debugLineNum = 265;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","georeferencedDate",(Object)(mostCurrent._main._fotopath3 /*String*/ ),_map1);
 //BA.debugLineNum = 266;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLatitude",(Object)(mostCurrent._main._latitud /*String*/ ),_map1);
 //BA.debugLineNum = 267;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLongitude",(Object)(mostCurrent._main._longitud /*String*/ ),_map1);
 //BA.debugLineNum = 268;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","georeferencedDate",(Object)(mostCurrent._main._dateandtime /*String*/ ),_map1);
 //BA.debugLineNum = 271;BA.debugLine="If Main.modooffline = True Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 272;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 274;BA.debugLine="MsgboxAsync(\"Esta trabajando en modo offline. E";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Esta trabajando en modo offline. El archivo se guardará para que lo pueda enviar luego desde 'Mi Perfil'"),BA.ObjectToCharSequence("Modo offline"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 276;BA.debugLine="MsgboxAsync(\"You are working offline. The repor";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("You are working offline. The report will be saved so you can send it later from 'Mi profile'"),BA.ObjectToCharSequence("Offline"),processBA);
 };
 //BA.debugLineNum = 278;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 279;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 280;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmprincipal.getObject()));
 //BA.debugLineNum = 281;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 284;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 285;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 286;BA.debugLine="msg = utilidades.Mensaje(\"Envío de datos\", \"MsgU";
_msg = mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Envío de datos","MsgUpload.png","Se enviarán los datos a un revisor especializado","Por defecto, el reporte que envías será de visibilidad pública. Si deseas hacerlo privado y que no sea exhibido en el sitio web cambia la opción desde tu perfil","Enviar datos","No enviar!","",anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 288;BA.debugLine="msg = utilidades.Mensaje(\"Uploading report\", \"Ms";
_msg = mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Uploading report","MsgUpload.png","The report will be sent to a specilized reviewer","By default, the report you are sending is public. You can make it private so it is not shown on the website by changing this option from your user profile","Send report","Do not send!","",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 290;BA.debugLine="If msg <> DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE)) == false) { 
 //BA.debugLineNum = 291;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 292;BA.debugLine="MsgboxAsync(\"Los datos quedarán guardados para";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Los datos quedarán guardados para que los envíe luego"),BA.ObjectToCharSequence("Datos guardados"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 296;BA.debugLine="MsgboxAsync(\"The report will be stored so you c";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("The report will be stored so you can send it later"),BA.ObjectToCharSequence("Report stored"),processBA);
 };
 //BA.debugLineNum = 299;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 300;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 301;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 304;BA.debugLine="btnFoto1.Enabled = False";
mostCurrent._btnfoto1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 305;BA.debugLine="btnFoto1.Visible = False";
mostCurrent._btnfoto1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 306;BA.debugLine="Foto_Dorsal.Visible = False";
mostCurrent._foto_dorsal.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 307;BA.debugLine="btnFoto2.Enabled = False";
mostCurrent._btnfoto2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 308;BA.debugLine="btnFoto2.Visible = False";
mostCurrent._btnfoto2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 309;BA.debugLine="Foto_Ventral.Visible = False";
mostCurrent._foto_ventral.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 310;BA.debugLine="chkPrivado.Visible = False";
mostCurrent._chkprivado.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 311;BA.debugLine="lblReportePublico.Visible = False";
mostCurrent._lblreportepublico.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 313;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 314;BA.debugLine="btnContinuar_Parte3.Text = \"\"";
mostCurrent._btncontinuar_parte3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 315;BA.debugLine="lblEnviar.text = \"Enviando...\"";
mostCurrent._lblenviar.setText(BA.ObjectToCharSequence("Enviando..."));
 //BA.debugLineNum = 316;BA.debugLine="lblInstrucciones.Text = \"Enviando... por favor a";
mostCurrent._lblinstrucciones.setText(BA.ObjectToCharSequence("Enviando... por favor aguarde"));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 318;BA.debugLine="btnContinuar_Parte3.Text = \"\"";
mostCurrent._btncontinuar_parte3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 319;BA.debugLine="lblEnviar.text = \"Uploading...\"";
mostCurrent._lblenviar.setText(BA.ObjectToCharSequence("Uploading..."));
 //BA.debugLineNum = 320;BA.debugLine="lblInstrucciones.Text = \"Uploading... please wai";
mostCurrent._lblinstrucciones.setText(BA.ObjectToCharSequence("Uploading... please wait"));
 };
 //BA.debugLineNum = 324;BA.debugLine="Log(\"Chequeando internet\")";
anywheresoftware.b4a.keywords.Common.LogImpl("429950053","Chequeando internet",0);
 //BA.debugLineNum = 325;BA.debugLine="CheckInternet";
_checkinternet();
 //BA.debugLineNum = 327;BA.debugLine="End Sub";
return "";
}
public static String  _btnfoto1_click() throws Exception{
 //BA.debugLineNum = 121;BA.debugLine="Sub btnFoto1_Click";
 //BA.debugLineNum = 122;BA.debugLine="currentFoto = \"dorsal\"";
_currentfoto = "dorsal";
 //BA.debugLineNum = 123;BA.debugLine="frmCamara.currentFoto = \"dorsal\"";
mostCurrent._frmcamara._currentfoto /*String*/  = "dorsal";
 //BA.debugLineNum = 124;BA.debugLine="StartActivity(frmCamara)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmcamara.getObject()));
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _btnfoto2_click() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub btnFoto2_Click";
 //BA.debugLineNum = 127;BA.debugLine="currentFoto = \"ventral\"";
_currentfoto = "ventral";
 //BA.debugLineNum = 128;BA.debugLine="frmCamara.currentFoto = \"ventral\"";
mostCurrent._frmcamara._currentfoto /*String*/  = "ventral";
 //BA.debugLineNum = 129;BA.debugLine="StartActivity(frmCamara)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmcamara.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _checkinternet() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 356;BA.debugLine="Sub CheckInternet";
 //BA.debugLineNum = 357;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 358;BA.debugLine="dd.url = Main.serverPath & \"/connect2/connecttest";
_dd.url /*String*/  = mostCurrent._main._serverpath /*String*/ +"/connect2/connecttest.php";
 //BA.debugLineNum = 359;BA.debugLine="dd.EventName = \"TestInternet\"";
_dd.EventName /*String*/  = "TestInternet";
 //BA.debugLineNum = 360;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmfotoscriadero.getObject();
 //BA.debugLineNum = 361;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._downloadservice.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 362;BA.debugLine="End Sub";
return "";
}
public static String  _enviardatos() throws Exception{
String _username = "";
String _dateandtime = "";
String _nombresitio = "";
String _tiporio = "";
String _lat = "";
String _lng = "";
String _gpsdetect = "";
String _wifidetect = "";
String _mapadetect = "";
String _valorind1 = "";
String _valorind2 = "";
String _valorind3 = "";
String _valorind4 = "";
String _valorind5 = "";
String _valorind6 = "";
String _valorind7 = "";
String _valorind8 = "";
String _valorind9 = "";
String _valorind10 = "";
String _valorind11 = "";
String _valorind12 = "";
String _valorind13 = "";
String _valorind14 = "";
String _valorind15 = "";
String _valorind16 = "";
String _valorind17 = "";
String _valorind18 = "";
String _valorind19 = "";
String _valorind20 = "";
String _terminado = "";
String _privado = "";
String _estadovalidacion = "";
String _deviceid = "";
anywheresoftware.b4a.objects.collections.Map _datosmap = null;
caza.mosquito.downloadservice._downloaddata _dd = null;
String _urlpath = "";
 //BA.debugLineNum = 386;BA.debugLine="Sub EnviarDatos";
 //BA.debugLineNum = 389;BA.debugLine="Dim username,dateandtime,nombresitio,tiporio,lat,";
_username = "";
_dateandtime = "";
_nombresitio = "";
_tiporio = "";
_lat = "";
_lng = "";
_gpsdetect = "";
_wifidetect = "";
_mapadetect = "";
 //BA.debugLineNum = 390;BA.debugLine="Dim valorind1,valorind2,valorind3,valorind4,valor";
_valorind1 = "";
_valorind2 = "";
_valorind3 = "";
_valorind4 = "";
_valorind5 = "";
_valorind6 = "";
_valorind7 = "";
_valorind8 = "";
_valorind9 = "";
_valorind10 = "";
_valorind11 = "";
_valorind12 = "";
_valorind13 = "";
_valorind14 = "";
_valorind15 = "";
_valorind16 = "";
_valorind17 = "";
_valorind18 = "";
_valorind19 = "";
_valorind20 = "";
 //BA.debugLineNum = 391;BA.debugLine="Dim terminado, privado,estadovalidacion, deviceID";
_terminado = "";
_privado = "";
_estadovalidacion = "";
_deviceid = "";
 //BA.debugLineNum = 394;BA.debugLine="Dim datosMap As Map";
_datosmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 395;BA.debugLine="datosMap.Initialize";
_datosmap.Initialize();
 //BA.debugLineNum = 396;BA.debugLine="datosMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SEL";
_datosmap = mostCurrent._dbutils._executemap /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local WHERE Id=?",new String[]{mostCurrent._main._currentproject /*String*/ });
 //BA.debugLineNum = 398;BA.debugLine="If datosMap = Null Or datosMap.IsInitialized = Fa";
if (_datosmap== null || _datosmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 399;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 400;BA.debugLine="ToastMessageShow(\"Error cargando el análisis\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error cargando el análisis"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 402;BA.debugLine="ToastMessageShow(\"Error loading the report\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error loading the report"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 404;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 406;BA.debugLine="username = datosMap.Get(\"usuario\")";
_username = BA.ObjectToString(_datosmap.Get((Object)("usuario")));
 //BA.debugLineNum = 407;BA.debugLine="dateandtime = datosMap.Get(\"georeferenceddate\")";
_dateandtime = BA.ObjectToString(_datosmap.Get((Object)("georeferenceddate")));
 //BA.debugLineNum = 408;BA.debugLine="nombresitio = datosMap.Get(\"nombresitio\")";
_nombresitio = BA.ObjectToString(_datosmap.Get((Object)("nombresitio")));
 //BA.debugLineNum = 409;BA.debugLine="tiporio = datosMap.Get(\"tipoeval\")";
_tiporio = BA.ObjectToString(_datosmap.Get((Object)("tipoeval")));
 //BA.debugLineNum = 410;BA.debugLine="lat = datosMap.Get(\"decimallatitude\")";
_lat = BA.ObjectToString(_datosmap.Get((Object)("decimallatitude")));
 //BA.debugLineNum = 411;BA.debugLine="lng = datosMap.Get(\"decimallongitude\")";
_lng = BA.ObjectToString(_datosmap.Get((Object)("decimallongitude")));
 //BA.debugLineNum = 412;BA.debugLine="gpsdetect = datosMap.Get(\"gpsdetect\")";
_gpsdetect = BA.ObjectToString(_datosmap.Get((Object)("gpsdetect")));
 //BA.debugLineNum = 413;BA.debugLine="wifidetect = datosMap.Get(\"wifidetect\")";
_wifidetect = BA.ObjectToString(_datosmap.Get((Object)("wifidetect")));
 //BA.debugLineNum = 414;BA.debugLine="mapadetect = datosMap.Get(\"mapadetect\")";
_mapadetect = BA.ObjectToString(_datosmap.Get((Object)("mapadetect")));
 //BA.debugLineNum = 415;BA.debugLine="valorind1 = datosMap.Get(\"par1\")";
_valorind1 = BA.ObjectToString(_datosmap.Get((Object)("par1")));
 //BA.debugLineNum = 416;BA.debugLine="valorind2 = datosMap.Get(\"par2\")";
_valorind2 = BA.ObjectToString(_datosmap.Get((Object)("par2")));
 //BA.debugLineNum = 417;BA.debugLine="valorind3 = datosMap.Get(\"par3\")";
_valorind3 = BA.ObjectToString(_datosmap.Get((Object)("par3")));
 //BA.debugLineNum = 418;BA.debugLine="valorind4 = datosMap.Get(\"par4\")";
_valorind4 = BA.ObjectToString(_datosmap.Get((Object)("par4")));
 //BA.debugLineNum = 419;BA.debugLine="valorind5 = datosMap.Get(\"par5\")";
_valorind5 = BA.ObjectToString(_datosmap.Get((Object)("par5")));
 //BA.debugLineNum = 420;BA.debugLine="valorind6 = datosMap.Get(\"par6\")";
_valorind6 = BA.ObjectToString(_datosmap.Get((Object)("par6")));
 //BA.debugLineNum = 421;BA.debugLine="valorind7 = datosMap.Get(\"par7\")";
_valorind7 = BA.ObjectToString(_datosmap.Get((Object)("par7")));
 //BA.debugLineNum = 422;BA.debugLine="valorind8 = datosMap.Get(\"par8\")";
_valorind8 = BA.ObjectToString(_datosmap.Get((Object)("par8")));
 //BA.debugLineNum = 423;BA.debugLine="valorind9 = datosMap.Get(\"par9\")";
_valorind9 = BA.ObjectToString(_datosmap.Get((Object)("par9")));
 //BA.debugLineNum = 424;BA.debugLine="valorind10 = datosMap.Get(\"par10\")";
_valorind10 = BA.ObjectToString(_datosmap.Get((Object)("par10")));
 //BA.debugLineNum = 425;BA.debugLine="foto1 = datosMap.Get(\"foto1\")";
mostCurrent._foto1 = BA.ObjectToString(_datosmap.Get((Object)("foto1")));
 //BA.debugLineNum = 426;BA.debugLine="foto2 = datosMap.Get(\"foto2\")";
mostCurrent._foto2 = BA.ObjectToString(_datosmap.Get((Object)("foto2")));
 //BA.debugLineNum = 427;BA.debugLine="foto3 = datosMap.Get(\"foto3\")";
mostCurrent._foto3 = BA.ObjectToString(_datosmap.Get((Object)("foto3")));
 //BA.debugLineNum = 429;BA.debugLine="foto4 = datosMap.Get(\"foto4\")";
mostCurrent._foto4 = BA.ObjectToString(_datosmap.Get((Object)("foto4")));
 //BA.debugLineNum = 430;BA.debugLine="terminado = datosMap.Get(\"terminado\")";
_terminado = BA.ObjectToString(_datosmap.Get((Object)("terminado")));
 //BA.debugLineNum = 431;BA.debugLine="privado = datosMap.Get(\"privado\")";
_privado = BA.ObjectToString(_datosmap.Get((Object)("privado")));
 //BA.debugLineNum = 432;BA.debugLine="If privado = Null Or privado = \"null\" Then";
if (_privado== null || (_privado).equals("null")) { 
 //BA.debugLineNum = 433;BA.debugLine="privado = \"no\"";
_privado = "no";
 };
 //BA.debugLineNum = 435;BA.debugLine="estadovalidacion = datosMap.Get(\"estadovalidacio";
_estadovalidacion = BA.ObjectToString(_datosmap.Get((Object)("estadovalidacion")));
 //BA.debugLineNum = 436;BA.debugLine="If estadovalidacion = \"null\" Then";
if ((_estadovalidacion).equals("null")) { 
 //BA.debugLineNum = 437;BA.debugLine="estadovalidacion = \"No Verificado\"";
_estadovalidacion = "No Verificado";
 };
 //BA.debugLineNum = 439;BA.debugLine="deviceID = datosMap.Get(\"deviceID\")";
_deviceid = BA.ObjectToString(_datosmap.Get((Object)("deviceID")));
 //BA.debugLineNum = 440;BA.debugLine="If deviceID = Null Or deviceID = \"\" Or deviceID";
if (_deviceid== null || (_deviceid).equals("") || (_deviceid).equals("null")) { 
 //BA.debugLineNum = 441;BA.debugLine="deviceID = utilidades.GetDeviceId";
_deviceid = mostCurrent._utilidades._getdeviceid /*String*/ (mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 445;BA.debugLine="Log(\"Comienza envio de datos\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430212155","Comienza envio de datos",0);
 //BA.debugLineNum = 448;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 450;BA.debugLine="If Main.tipoevaluacion = \"Mosquito\" Then";
if ((mostCurrent._main._tipoevaluacion /*String*/ ).equals("Mosquito")) { 
 //BA.debugLineNum = 451;BA.debugLine="Dim urlpath As String";
_urlpath = "";
 //BA.debugLineNum = 452;BA.debugLine="urlpath = Main.serverPath & \"/connect2/addpuntom";
_urlpath = mostCurrent._main._serverpath /*String*/ +"/connect2/addpuntomosquito.php?"+"username="+_username+"&"+"deviceID="+mostCurrent._main._deviceid /*String*/ +"&"+"dateandtime="+_dateandtime+"&"+"tipoevaluacion="+"Mosquito"+"&"+"lat="+_lat+"&"+"lng="+_lng+"&"+"valorMosquito="+_valorind1+"&"+"foto1path="+mostCurrent._foto1+"&"+"foto2path="+mostCurrent._foto2+"&"+"foto3path="+mostCurrent._foto3+"&"+"foto4path="+mostCurrent._foto4+"&"+"privado="+_privado+"&"+"gpsdetect="+_gpsdetect+"&"+"wifidetect="+_wifidetect+"&"+"mapadetect="+_mapadetect+"&"+"terminado="+_terminado+"&"+"verificado=No Verificado";
 //BA.debugLineNum = 464;BA.debugLine="dd.url = urlpath";
_dd.url /*String*/  = _urlpath;
 }else {
 //BA.debugLineNum = 466;BA.debugLine="dd.url = Main.serverPath & \"/connect2/addpuntocr";
_dd.url /*String*/  = mostCurrent._main._serverpath /*String*/ +"/connect2/addpuntocriadero.php?"+"username="+_username+"&"+"deviceID="+mostCurrent._main._deviceid /*String*/ +"&"+"dateandtime="+_dateandtime+"&"+"tipoevaluacion="+"Criadero"+"&"+"lat="+_lat+"&"+"lng="+_lng+"&"+"valorCriadero="+_valorind1+"&"+"valorCriadero1="+_valorind2+"&"+"valorCriadero2="+_valorind3+"&"+"foto1path="+mostCurrent._foto1+"&"+"foto2path="+mostCurrent._foto2+"&"+"foto3path="+mostCurrent._foto3+"&"+"foto4path="+mostCurrent._foto4+"&"+"privado="+_privado+"&"+"gpsdetect="+_gpsdetect+"&"+"wifidetect="+_wifidetect+"&"+"mapadetect="+_mapadetect+"&"+"terminado="+_terminado+"&"+"verificado=No Verificado";
 };
 //BA.debugLineNum = 482;BA.debugLine="dd.EventName = \"EnviarDatos\"";
_dd.EventName /*String*/  = "EnviarDatos";
 //BA.debugLineNum = 483;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmfotoscriadero.getObject();
 //BA.debugLineNum = 484;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._downloadservice.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _enviardatos_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _nd = null;
String _serverid = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 487;BA.debugLine="Sub EnviarDatos_Complete(Job As HttpJob)";
 //BA.debugLineNum = 488;BA.debugLine="Log(\"Datos enviados : \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("430277633","Datos enviados : "+BA.ObjectToString(_job._success /*boolean*/ ),0);
 //BA.debugLineNum = 489;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 490;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 491;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 492;BA.debugLine="ret = Job.GetString";
_ret = _job._getstring /*String*/ ();
 //BA.debugLineNum = 493;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 494;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 495;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 496;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 497;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 498;BA.debugLine="ToastMessageShow(\"Error en la carga de marcado";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error en la carga de marcadores"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 500;BA.debugLine="ToastMessageShow(\"Error loading markers\", True";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error loading markers"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 502;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 }else if((_act).equals("Marcadores")) { 
 //BA.debugLineNum = 504;BA.debugLine="Dim nd As Map";
_nd = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 505;BA.debugLine="nd = parser.NextObject";
_nd = _parser.NextObject();
 //BA.debugLineNum = 506;BA.debugLine="Dim serverID As String";
_serverid = "";
 //BA.debugLineNum = 507;BA.debugLine="serverID = nd.Get(\"serverId\")";
_serverid = BA.ObjectToString(_nd.Get((Object)("serverId")));
 //BA.debugLineNum = 510;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 511;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 512;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 513;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","evalsent",(Object)("si"),_map1);
 //BA.debugLineNum = 514;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","serverId",(Object)(_serverid),_map1);
 //BA.debugLineNum = 515;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 516;BA.debugLine="ToastMessageShow(\"Datos enviados, enviando fot";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Datos enviados, enviando fotos"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 518;BA.debugLine="ToastMessageShow(\"Report sent, sending photos\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report sent, sending photos"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 523;BA.debugLine="EnviarFotos";
_enviarfotos();
 }else if((_act).equals("ErrorCoord")) { 
 //BA.debugLineNum = 526;BA.debugLine="MsgboxAsync(\"Hay un error con las coordenadas d";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Hay un error con las coordenadas del reporte, prueba rehacerlo y volver a seleccionar en el mapa tu posición"),BA.ObjectToCharSequence("Error GPS"),processBA);
 //BA.debugLineNum = 527;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 528;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 }else {
 //BA.debugLineNum = 531;BA.debugLine="Log(\"envio datos not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430277676","envio datos not ok",0);
 //BA.debugLineNum = 532;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 533;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 535;BA.debugLine="MsgboxAsync(\"There seems to be a problem with o";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with our servers, we will solve it soon!"),BA.ObjectToCharSequence("My bad"),processBA);
 };
 //BA.debugLineNum = 537;BA.debugLine="btnContinuar_Parte3.Text = \"\"";
mostCurrent._btncontinuar_parte3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 538;BA.debugLine="lblInstrucciones.Text = \"Intente enviar los dato";
mostCurrent._lblinstrucciones.setText(BA.ObjectToCharSequence("Intente enviar los datos de nuevo"));
 };
 //BA.debugLineNum = 541;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 542;BA.debugLine="End Sub";
return "";
}
public static String  _enviarfotos() throws Exception{
 //BA.debugLineNum = 550;BA.debugLine="Sub EnviarFotos";
 //BA.debugLineNum = 553;BA.debugLine="totalFotos = 2";
_totalfotos = (int) (2);
 //BA.debugLineNum = 555;BA.debugLine="Up1.B4A_log=True";
_up1.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 556;BA.debugLine="Up1.Initialize(\"Up1\")";
_up1.Initialize(processBA,"Up1");
 //BA.debugLineNum = 558;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMosqu";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._foto1+".jpg") && _foto1sent==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 559;BA.debugLine="Log(\"Enviando foto 1 \")";
anywheresoftware.b4a.keywords.Common.LogImpl("430343177","Enviando foto 1 ",0);
 //BA.debugLineNum = 560;BA.debugLine="ProgressBar1.Visible = True";
mostCurrent._progressbar1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 561;BA.debugLine="Up1.doFileUpload(ProgressBar1,Null,File.DirRootE";
_up1.doFileUpload(processBA,(android.widget.ProgressBar)(mostCurrent._progressbar1.getObject()),(android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/"+mostCurrent._foto1+".jpg",mostCurrent._main._serverpath /*String*/ +"/connect2/upload_file.php");
 //BA.debugLineNum = 562;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 563;BA.debugLine="lblStatus.Text = \"Enviando foto 1...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Enviando foto 1..."));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 565;BA.debugLine="lblStatus.Text = \"Uploading foto 1...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Uploading foto 1..."));
 };
 }else {
 //BA.debugLineNum = 568;BA.debugLine="Log(\"no foto 1\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430343186","no foto 1",0);
 };
 //BA.debugLineNum = 572;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private Foto_Ventral As ImageView";
mostCurrent._foto_ventral = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private Foto_Dorsal As ImageView";
mostCurrent._foto_dorsal = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private btnContinuar_Parte3 As Button";
mostCurrent._btncontinuar_parte3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private btnFoto1 As Button";
mostCurrent._btnfoto1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private btnFoto2 As Button";
mostCurrent._btnfoto2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private ProgressBar2 As ProgressBar";
mostCurrent._progressbar2 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblEnviar As Label";
mostCurrent._lblenviar = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblFondoText As Label";
mostCurrent._lblfondotext = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private txtAccion As EditText";
mostCurrent._txtaccion = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private radNoAccion As RadioButton";
mostCurrent._radnoaccion = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private radSiAccion As RadioButton";
mostCurrent._radsiaccion = new anywheresoftware.b4a.objects.CompoundButtonWrapper.RadioButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim foto1, foto2, foto3, foto4 As String";
mostCurrent._foto1 = "";
mostCurrent._foto2 = "";
mostCurrent._foto3 = "";
mostCurrent._foto4 = "";
 //BA.debugLineNum = 34;BA.debugLine="Private totalFotos As Int";
_totalfotos = 0;
 //BA.debugLineNum = 35;BA.debugLine="Private foto1Sent As Boolean";
_foto1sent = false;
 //BA.debugLineNum = 36;BA.debugLine="Private foto2Sent As Boolean";
_foto2sent = false;
 //BA.debugLineNum = 37;BA.debugLine="Dim fotosEnviadas As Int";
_fotosenviadas = 0;
 //BA.debugLineNum = 38;BA.debugLine="Dim pw As PhoneWakeState";
mostCurrent._pw = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 39;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 40;BA.debugLine="Dim hizoAlgo As Boolean";
_hizoalgo = false;
 //BA.debugLineNum = 43;BA.debugLine="Dim foto_dorsal_existe As Boolean = False";
_foto_dorsal_existe = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 44;BA.debugLine="Dim foto_ventral_existe As Boolean = False";
_foto_ventral_existe = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 45;BA.debugLine="Private lblInstrucciones As Label";
mostCurrent._lblinstrucciones = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lblReportePublico As Label";
mostCurrent._lblreportepublico = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private chkPrivado As CheckBox";
mostCurrent._chkprivado = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private lblStatus As Label";
mostCurrent._lblstatus = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lblPaso3 As Label";
mostCurrent._lblpaso3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _lblreportepublico_click() throws Exception{
 //BA.debugLineNum = 343;BA.debugLine="Sub lblReportePublico_Click";
 //BA.debugLineNum = 344;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 345;BA.debugLine="utilidades.Mensaje(\"Privacidad\", Null, \"Los repo";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Privacidad",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),"Los reportes públicos podrán ser vistos desde el mapa de Caza Mosquitos, aunque aparecerá como 'Dato no validado' hasta que un especialista lo compruebe. Puedes elegir que el reporte sea privado, o cambiarlo luego desde 'Mis Datos Anteriores'","","Ok, entendido!",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 347;BA.debugLine="utilidades.Mensaje(\"Privacy\", Null, \"Public repo";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Privacy",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),"Public reports will be displayed in the Caza Mosquitos map, although they will show as 'Not validated' until a specialist checks it. You can choose to make it private, or later change it through the menu 'My previous reports'","","Ok, got it!",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 349;BA.debugLine="End Sub";
return "";
}
public static String  _previewfotos() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub PreviewFotos";
 //BA.debugLineNum = 133;BA.debugLine="Log(Main.fotopath0)";
anywheresoftware.b4a.keywords.Common.LogImpl("429622273",mostCurrent._main._fotopath0 /*String*/ ,0);
 //BA.debugLineNum = 134;BA.debugLine="Log(Main.fotopath1)";
anywheresoftware.b4a.keywords.Common.LogImpl("429622274",mostCurrent._main._fotopath1 /*String*/ ,0);
 //BA.debugLineNum = 135;BA.debugLine="Log(Main.fotopath3)";
anywheresoftware.b4a.keywords.Common.LogImpl("429622275",mostCurrent._main._fotopath3 /*String*/ ,0);
 //BA.debugLineNum = 138;BA.debugLine="If currentFoto = \"dorsal\" Then";
if ((_currentfoto).equals("dorsal")) { 
 //BA.debugLineNum = 139;BA.debugLine="If Main.fotopath0 <> \"\" Then";
if ((mostCurrent._main._fotopath0 /*String*/ ).equals("") == false) { 
 //BA.debugLineNum = 140;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMos";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._main._fotopath0 /*String*/ +".jpg")) { 
 //BA.debugLineNum = 141;BA.debugLine="Foto_Dorsal.Bitmap = Null";
mostCurrent._foto_dorsal.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 142;BA.debugLine="Foto_Dorsal.Bitmap = LoadBitmapSample(File.Dir";
mostCurrent._foto_dorsal.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._main._fotopath0 /*String*/ +".jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 //BA.debugLineNum = 143;BA.debugLine="foto_dorsal_existe = True";
_foto_dorsal_existe = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 145;BA.debugLine="foto_dorsal_existe = False";
_foto_dorsal_existe = anywheresoftware.b4a.keywords.Common.False;
 };
 };
 }else if((_currentfoto).equals("ventral")) { 
 //BA.debugLineNum = 149;BA.debugLine="If Main.fotopath1 <> \"\" Then";
if ((mostCurrent._main._fotopath1 /*String*/ ).equals("") == false) { 
 //BA.debugLineNum = 150;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMos";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._main._fotopath1 /*String*/ +".jpg")) { 
 //BA.debugLineNum = 151;BA.debugLine="Foto_Ventral.Bitmap = Null";
mostCurrent._foto_ventral.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 152;BA.debugLine="Foto_Ventral.Bitmap = LoadBitmapSample(File.Di";
mostCurrent._foto_ventral.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._main._fotopath1 /*String*/ +".jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 //BA.debugLineNum = 153;BA.debugLine="foto_ventral_existe = True";
_foto_ventral_existe = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 155;BA.debugLine="foto_ventral_existe = False";
_foto_ventral_existe = anywheresoftware.b4a.keywords.Common.False;
 };
 };
 };
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim Up1 As UploadFilePhp";
_up1 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 10;BA.debugLine="Dim Up2 As UploadFilePhp";
_up2 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 13;BA.debugLine="Dim currentFoto As String";
_currentfoto = "";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _radnoaccion_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 213;BA.debugLine="Sub radNoAccion_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 214;BA.debugLine="lblFondoText.Visible = False";
mostCurrent._lblfondotext.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 215;BA.debugLine="txtAccion.Enabled = False";
mostCurrent._txtaccion.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return "";
}
public static String  _radsiaccion_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub radSiAccion_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 219;BA.debugLine="lblFondoText.Visible = True";
mostCurrent._lblfondotext.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 220;BA.debugLine="txtAccion.Enabled = True";
mostCurrent._txtaccion.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public static String  _testinternet_complete(caza.mosquito.httpjob _job) throws Exception{
 //BA.debugLineNum = 363;BA.debugLine="Sub TestInternet_Complete(Job As HttpJob)";
 //BA.debugLineNum = 364;BA.debugLine="Log(\"Chequeo de internet: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("430146561","Chequeo de internet: "+BA.ObjectToString(_job._success /*boolean*/ ),0);
 //BA.debugLineNum = 365;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 367;BA.debugLine="Main.modooffline = False";
mostCurrent._main._modooffline /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 368;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 370;BA.debugLine="EnviarDatos";
_enviardatos();
 }else {
 //BA.debugLineNum = 373;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 374;BA.debugLine="MsgboxAsync(\"No hay conexión a internet, prueba";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No hay conexión a internet, prueba cuando estés conectado!"),BA.ObjectToCharSequence("No hay internet"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 376;BA.debugLine="MsgboxAsync(\"No internet connection, try again";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No internet connection, try again later!"),BA.ObjectToCharSequence("No internet"),processBA);
 };
 //BA.debugLineNum = 378;BA.debugLine="Main.modooffline = True";
mostCurrent._main._modooffline /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 379;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 380;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 381;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 382;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 };
 //BA.debugLineNum = 384;BA.debugLine="End Sub";
return "";
}
public static String  _up1_sendfile(String _value) throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 579;BA.debugLine="Sub Up1_sendFile (value As String)";
 //BA.debugLineNum = 580;BA.debugLine="Log(\"sendfile event:\" & value)";
anywheresoftware.b4a.keywords.Common.LogImpl("430474241","sendfile event:"+_value,0);
 //BA.debugLineNum = 581;BA.debugLine="If value = \"success\" Then";
if ((_value).equals("success")) { 
 //BA.debugLineNum = 583;BA.debugLine="If foto1 <> \"null\" And ProgressBar1.IsInitialize";
if ((mostCurrent._foto1).equals("null") == false && mostCurrent._progressbar1.IsInitialized() && mostCurrent._progressbar1.getProgress()==100) { 
 //BA.debugLineNum = 584;BA.debugLine="Log(\"TERMINO EL ENVIO FOTO 1\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430474245","TERMINO EL ENVIO FOTO 1",0);
 //BA.debugLineNum = 585;BA.debugLine="foto1Sent = True";
_foto1sent = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 586;BA.debugLine="fotosEnviadas = fotosEnviadas+ 1";
_fotosenviadas = (int) (_fotosenviadas+1);
 //BA.debugLineNum = 588;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 589;BA.debugLine="If hizoAlgo = True Then";
if (_hizoalgo==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 590;BA.debugLine="Up2.B4A_log=True";
_up2.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 591;BA.debugLine="Up2.Initialize(\"Up2\")";
_up2.Initialize(processBA,"Up2");
 //BA.debugLineNum = 592;BA.debugLine="If File.Exists(File.DirRootExternal & \"/CazaMo";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._foto2+".jpg") && _foto2sent==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 593;BA.debugLine="Log(\"Enviando foto 2 \")";
anywheresoftware.b4a.keywords.Common.LogImpl("430474254","Enviando foto 2 ",0);
 //BA.debugLineNum = 594;BA.debugLine="ProgressBar2.Visible = True";
mostCurrent._progressbar2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 595;BA.debugLine="Up2.doFileUpload(ProgressBar2,Null,File.DirRo";
_up2.doFileUpload(processBA,(android.widget.ProgressBar)(mostCurrent._progressbar2.getObject()),(android.widget.TextView)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/"+mostCurrent._foto2+".jpg",mostCurrent._main._serverpath /*String*/ +"/connect2/upload_file.php");
 //BA.debugLineNum = 597;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 598;BA.debugLine="lblStatus.Text = \"Enviando foto 2...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Enviando foto 2..."));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 600;BA.debugLine="lblStatus.Text = \"Uploading photo 2...\"";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence("Uploading photo 2..."));
 };
 }else {
 //BA.debugLineNum = 603;BA.debugLine="Log(\"no foto 2\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430474264","no foto 2",0);
 };
 }else {
 //BA.debugLineNum = 607;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 608;BA.debugLine="ToastMessageShow(\"Evaluación enviada\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Evaluación enviada"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 610;BA.debugLine="ToastMessageShow(\"Report sent\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report sent"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 613;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 614;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 615;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 616;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_l";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1sent",(Object)("si"),_map1);
 //BA.debugLineNum = 617;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_l";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2sent",(Object)("si"),_map1);
 //BA.debugLineNum = 618;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 619;BA.debugLine="utilidades.Mensaje(\"Felicitaciones!\", \"MsgIco";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Felicitaciones!","MsgIcon.png","Envío exitoso","Usted ha enviado las fotos correctamente y ya puede intentar determinar que tipo de mosquito encontró.","OK, continuar","","",anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 621;BA.debugLine="utilidades.Mensaje(\"Congratulations!\", \"MsgIc";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Congratulations!","MsgIcon.png","Upload successful","The photos have been uploaded correctly, and you can try to determine which kind of mosquito you found.","OK, continue","","",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 623;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 624;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 };
 };
 //BA.debugLineNum = 628;BA.debugLine="Log(\"FOTO #\" & fotosEnviadas & \"/\" & totalFotos";
anywheresoftware.b4a.keywords.Common.LogImpl("430474289","FOTO #"+BA.NumberToString(_fotosenviadas)+"/"+BA.NumberToString(_totalfotos)+" ENVIADA",0);
 }else if((_value).equals("Error!")) { 
 //BA.debugLineNum = 631;BA.debugLine="Log(\"FOTO error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430474292","FOTO error",0);
 //BA.debugLineNum = 632;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 633;BA.debugLine="MsgboxAsync(\"Ha habido un error en el envío. Re";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ha habido un error en el envío. Revisa tu conexión a Internet e intenta de nuevo desde 'Datos Anteriores'"),BA.ObjectToCharSequence("Oops!"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 635;BA.debugLine="MsgboxAsync(\"Upload error. Check your connectio";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Upload error. Check your connection and try again from 'My profile'"),BA.ObjectToCharSequence("Oops!"),processBA);
 };
 //BA.debugLineNum = 637;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 638;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 //BA.debugLineNum = 639;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 640;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 641;BA.debugLine="Activity_Create(False)";
_activity_create(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 643;BA.debugLine="End Sub";
return "";
}
public static String  _up1_statusupload(String _value) throws Exception{
 //BA.debugLineNum = 576;BA.debugLine="Sub Up1_statusUpload (value As String)";
 //BA.debugLineNum = 577;BA.debugLine="End Sub";
return "";
}
public static String  _up1_statusvisible(boolean _onoff,String _value) throws Exception{
 //BA.debugLineNum = 644;BA.debugLine="Sub Up1_statusVISIBLE (onoff As Boolean,value As S";
 //BA.debugLineNum = 646;BA.debugLine="End Sub";
return "";
}
public static String  _up2_sendfile(String _value) throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 652;BA.debugLine="Sub Up2_sendFile (value As String)";
 //BA.debugLineNum = 653;BA.debugLine="Log(\"sendfile event:\" & value)";
anywheresoftware.b4a.keywords.Common.LogImpl("430670849","sendfile event:"+_value,0);
 //BA.debugLineNum = 654;BA.debugLine="If value = \"success\" Then";
if ((_value).equals("success")) { 
 //BA.debugLineNum = 656;BA.debugLine="If foto2 <> \"null\" And ProgressBar2.IsInitialize";
if ((mostCurrent._foto2).equals("null") == false && mostCurrent._progressbar2.IsInitialized() && mostCurrent._progressbar2.getProgress()==100) { 
 //BA.debugLineNum = 657;BA.debugLine="Log(\"TERMINO EL ENVIO FOTO 2\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430670853","TERMINO EL ENVIO FOTO 2",0);
 //BA.debugLineNum = 658;BA.debugLine="foto2Sent = True";
_foto2sent = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 659;BA.debugLine="fotosEnviadas = fotosEnviadas+ 1";
_fotosenviadas = (int) (_fotosenviadas+1);
 //BA.debugLineNum = 661;BA.debugLine="Log(\"TODAS LAS FOTOS FUERON ENVIADAS CORRECTAME";
anywheresoftware.b4a.keywords.Common.LogImpl("430670857","TODAS LAS FOTOS FUERON ENVIADAS CORRECTAMENTE",0);
 //BA.debugLineNum = 662;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 663;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 //BA.debugLineNum = 664;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 665;BA.debugLine="ToastMessageShow(\"Evaluación enviada\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Evaluación enviada"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 667;BA.debugLine="ToastMessageShow(\"Report sent\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Report sent"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 670;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 671;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 672;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 673;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1sent",(Object)("si"),_map1);
 //BA.debugLineNum = 674;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2sent",(Object)("si"),_map1);
 //BA.debugLineNum = 675;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 676;BA.debugLine="utilidades.Mensaje(\"Felicitaciones!\", \"MsgIcon";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Felicitaciones!","MsgIcon.png","Envío exitoso","Usted ha enviado las fotos correctamente y ya puede intentar determinar que tipo de mosquito encontró.","OK, continuar","","",anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 678;BA.debugLine="utilidades.Mensaje(\"Congratulations!\", \"MsgIco";
mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Congratulations!","MsgIcon.png","Upload successful","The photos have been uploaded correctly, and you can try to determine which kind of mosquito you found.","OK, continue","","",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 680;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 681;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 685;BA.debugLine="Log(\"FOTO #\" & fotosEnviadas & \"/\" & totalFotos";
anywheresoftware.b4a.keywords.Common.LogImpl("430670881","FOTO #"+BA.NumberToString(_fotosenviadas)+"/"+BA.NumberToString(_totalfotos)+" ENVIADA",0);
 }else if((_value).equals("Error!")) { 
 //BA.debugLineNum = 687;BA.debugLine="Log(\"FOTO error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("430670883","FOTO error",0);
 //BA.debugLineNum = 688;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 689;BA.debugLine="MsgboxAsync(\"Ha habido un error en el envío. Re";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ha habido un error en el envío. Revisa tu conexión a Internet e intenta de nuevo desde 'Datos Anteriores'"),BA.ObjectToCharSequence("Oops!"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 691;BA.debugLine="MsgboxAsync(\"Upload error. Check your connectio";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Upload error. Check your connection and try again from 'My profile'"),BA.ObjectToCharSequence("Oops!"),processBA);
 };
 //BA.debugLineNum = 693;BA.debugLine="Up1.UploadKill";
_up1.UploadKill(processBA);
 //BA.debugLineNum = 694;BA.debugLine="Up2.UploadKill";
_up2.UploadKill(processBA);
 //BA.debugLineNum = 695;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 696;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 697;BA.debugLine="Activity_Create(False)";
_activity_create(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 699;BA.debugLine="End Sub";
return "";
}
public static String  _up2_statusupload(String _value) throws Exception{
 //BA.debugLineNum = 649;BA.debugLine="Sub Up2_statusUpload (value As String)";
 //BA.debugLineNum = 650;BA.debugLine="End Sub";
return "";
}
public static String  _up2_statusvisible(boolean _onoff,String _value) throws Exception{
 //BA.debugLineNum = 700;BA.debugLine="Sub Up2_statusVISIBLE (onoff As Boolean,value As S";
 //BA.debugLineNum = 702;BA.debugLine="End Sub";
return "";
}
}
