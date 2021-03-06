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

public class register extends Activity implements B4AActivity{
	public static register mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.register");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (register).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.register");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.register", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (register) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (register) Resume **");
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
		return register.class;
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
            BA.LogInfo("** Activity (register) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (register) Pause event (activity is not paused). **");
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
            register mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (register) Resume **");
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
public anywheresoftware.b4a.objects.EditTextWrapper _txtuserid = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpassword2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtemail = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblunmaskpass = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public caza.mosquito.main _main = null;
public caza.mosquito.frmcamara _frmcamara = null;
public caza.mosquito.frmquehacer _frmquehacer = null;
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
public caza.mosquito.frmfotoscriadero _frmfotoscriadero = null;
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
 //BA.debugLineNum = 19;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 20;BA.debugLine="Activity.LoadLayout(\"layRegister\")";
mostCurrent._activity.LoadLayout("layRegister",mostCurrent.activityBA);
 //BA.debugLineNum = 21;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 25;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 26;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 27;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 };
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 32;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _btncancelarregistro_click() throws Exception{
 //BA.debugLineNum = 185;BA.debugLine="Sub btnCancelarRegistro_Click";
 //BA.debugLineNum = 186;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 187;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 188;BA.debugLine="End Sub";
return "";
}
public static String  _btnregister_click() throws Exception{
String _struserid = "";
String _strpassword = "";
String _strpassword2 = "";
String _stremail = "";
 //BA.debugLineNum = 56;BA.debugLine="Sub btnRegister_Click";
 //BA.debugLineNum = 57;BA.debugLine="Dim strUserID As String = txtUserID.Text.Trim";
_struserid = mostCurrent._txtuserid.getText().trim();
 //BA.debugLineNum = 58;BA.debugLine="If strUserID = \"\" Then";
if ((_struserid).equals("")) { 
 //BA.debugLineNum = 59;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 60;BA.debugLine="MsgboxAsync(\"Ingrese nombre de usuario\", \"Error";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ingrese nombre de usuario"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 62;BA.debugLine="MsgboxAsync(\"Enter a username\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Enter a username"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 64;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 66;BA.debugLine="Dim strPassword As String = txtPassword.Text.Trim";
_strpassword = mostCurrent._txtpassword.getText().trim();
 //BA.debugLineNum = 67;BA.debugLine="If strPassword = \"\" Then";
if ((_strpassword).equals("")) { 
 //BA.debugLineNum = 68;BA.debugLine="If Main.lang = \"en\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 69;BA.debugLine="MsgboxAsync(\"Enter a password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Enter a password"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 71;BA.debugLine="MsgboxAsync(\"Ingrese contraseña\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ingrese contraseña"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 73;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 75;BA.debugLine="Dim strPassword2 As String = txtPassword2.Text.Tr";
_strpassword2 = mostCurrent._txtpassword2.getText().trim();
 //BA.debugLineNum = 76;BA.debugLine="If strPassword2 = \"\" Then";
if ((_strpassword2).equals("")) { 
 //BA.debugLineNum = 77;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 78;BA.debugLine="MsgboxAsync(\"Confirme su contraseña\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Confirme su contraseña"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 80;BA.debugLine="MsgboxAsync(\"Confirm your password\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Confirm your password"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 82;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 84;BA.debugLine="If strPassword <> strPassword2 Then";
if ((_strpassword).equals(_strpassword2) == false) { 
 //BA.debugLineNum = 85;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 86;BA.debugLine="MsgboxAsync(\"Las contraseñas no coinciden\", \"Er";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Las contraseñas no coinciden"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 88;BA.debugLine="MsgboxAsync(\"Passwords don't match\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Passwords don't match"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 90;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 93;BA.debugLine="Dim strEmail As String = txtEmail.Text.Trim";
_stremail = mostCurrent._txtemail.getText().trim();
 //BA.debugLineNum = 94;BA.debugLine="If strEmail = \"\" Then";
if ((_stremail).equals("")) { 
 //BA.debugLineNum = 95;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 96;BA.debugLine="MsgboxAsync(\"Ingrese email\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Ingrese email"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 98;BA.debugLine="MsgboxAsync(\"Enter your email\", \"Error\")";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Enter your email"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 100;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 102;BA.debugLine="If Validate_Email(strEmail) = False Then";
if (_validate_email(_stremail)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 103;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 104;BA.debugLine="MsgboxAsync(\"Formato de email incorrecto\", \"Err";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Formato de email incorrecto"),BA.ObjectToCharSequence("Error"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 106;BA.debugLine="MsgboxAsync(\"Incorrect format for the email add";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Incorrect format for the email address"),BA.ObjectToCharSequence("Error"),processBA);
 };
 //BA.debugLineNum = 108;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 111;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 112;BA.debugLine="ProgressDialogShow(\"Registrando el usuario...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Registrando el usuario..."));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 114;BA.debugLine="ProgressDialogShow(\"Signing up user...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Signing up user..."));
 };
 //BA.debugLineNum = 118;BA.debugLine="RegisterUser";
_registeruser();
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Private txtUserID As EditText";
mostCurrent._txtuserid = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Private txtPassword As EditText";
mostCurrent._txtpassword = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private txtPassword2 As EditText";
mostCurrent._txtpassword2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private txtEmail As EditText";
mostCurrent._txtemail = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private lblUnmaskPass As Label";
mostCurrent._lblunmaskpass = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static String  _lblleerlegales_click() throws Exception{
 //BA.debugLineNum = 196;BA.debugLine="Sub lblLeerLegales_Click";
 //BA.debugLineNum = 197;BA.debugLine="StartActivity(frmPoliticaDatos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmpoliticadatos.getObject()));
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _lblunmaskpass_click() throws Exception{
 //BA.debugLineNum = 44;BA.debugLine="Sub lblUnmaskPass_Click";
 //BA.debugLineNum = 45;BA.debugLine="If lblUnmaskPass.Text = \"\" Then";
if ((mostCurrent._lblunmaskpass.getText()).equals("")) { 
 //BA.debugLineNum = 46;BA.debugLine="txtPassword.PasswordMode = True";
mostCurrent._txtpassword.setPasswordMode(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 47;BA.debugLine="txtPassword2.PasswordMode = True";
mostCurrent._txtpassword2.setPasswordMode(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 48;BA.debugLine="lblUnmaskPass.Text = \"\"";
mostCurrent._lblunmaskpass.setText(BA.ObjectToCharSequence(""));
 }else if((mostCurrent._lblunmaskpass.getText()).equals("")) { 
 //BA.debugLineNum = 50;BA.debugLine="txtPassword.PasswordMode = False";
mostCurrent._txtpassword.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 51;BA.debugLine="txtPassword2.PasswordMode = False";
mostCurrent._txtpassword2.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 52;BA.debugLine="lblUnmaskPass.Text = \"\"";
mostCurrent._lblunmaskpass.setText(BA.ObjectToCharSequence(""));
 };
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _registeruser() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 120;BA.debugLine="Sub RegisterUser";
 //BA.debugLineNum = 123;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 124;BA.debugLine="dd.url = Main.serverPath & \"/connect2/signupUser.";
_dd.url /*String*/  = mostCurrent._main._serverpath /*String*/ +"/connect2/signupUser.php?Action=RegisterNew&"+"UserID="+mostCurrent._txtuserid.getText()+"&"+"Password="+mostCurrent._txtpassword.getText()+"&"+"Email="+mostCurrent._txtemail.getText()+"&"+"deviceID="+mostCurrent._main._deviceid /*String*/ ;
 //BA.debugLineNum = 130;BA.debugLine="dd.EventName = \"RegisterUser\"";
_dd.EventName /*String*/  = "RegisterUser";
 //BA.debugLineNum = 131;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = register.getObject();
 //BA.debugLineNum = 132;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._downloadservice.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _registeruser_complete(caza.mosquito.httpjob _job) throws Exception{
String _res = "";
String _action = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
 //BA.debugLineNum = 135;BA.debugLine="Sub RegisterUser_Complete(Job As HttpJob)";
 //BA.debugLineNum = 136;BA.debugLine="Log(\"Register user: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("43801089","Register user: "+BA.ObjectToString(_job._success /*boolean*/ ),0);
 //BA.debugLineNum = 137;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 138;BA.debugLine="Dim res As String, action As String";
_res = "";
_action = "";
 //BA.debugLineNum = 139;BA.debugLine="res = Job.GetString";
_res = _job._getstring /*String*/ ();
 //BA.debugLineNum = 140;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 141;BA.debugLine="parser.Initialize(res)";
_parser.Initialize(_res);
 //BA.debugLineNum = 142;BA.debugLine="action = parser.NextValue";
_action = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 143;BA.debugLine="If action = \"Mail\" Then";
if ((_action).equals("Mail")) { 
 //BA.debugLineNum = 144;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 145;BA.debugLine="ToastMessageShow(\"Usuario registrado correctam";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Usuario registrado correctamente!"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 147;BA.debugLine="ToastMessageShow(\"Usuario registrado correctam";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Usuario registrado correctamente!"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 149;BA.debugLine="Main.username = txtUserID.Text";
mostCurrent._main._username /*String*/  = mostCurrent._txtuserid.getText();
 //BA.debugLineNum = 150;BA.debugLine="Main.pass = txtPassword.Text";
mostCurrent._main._pass /*String*/  = mostCurrent._txtpassword.getText();
 //BA.debugLineNum = 151;BA.debugLine="Main.strUserName = Main.username";
mostCurrent._main._strusername /*String*/  = mostCurrent._main._username /*String*/ ;
 //BA.debugLineNum = 152;BA.debugLine="frmLogin.registerOK = True";
mostCurrent._frmlogin._registerok /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 153;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 154;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else if((_action).equals("MailInUse")) { 
 //BA.debugLineNum = 156;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 157;BA.debugLine="MsgboxAsync(\"El usuario '\" & txtUserID.Text &";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("El usuario '"+mostCurrent._txtuserid.getText()+"' o el email ("+mostCurrent._txtemail.getText()+") ya están en uso"),BA.ObjectToCharSequence("Registro"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 159;BA.debugLine="MsgboxAsync(\"The username '\" & txtUserID.Text";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("The username '"+mostCurrent._txtuserid.getText()+"' or the email ("+mostCurrent._txtemail.getText()+") already exist"),BA.ObjectToCharSequence("Sign up"),processBA);
 };
 }else {
 //BA.debugLineNum = 162;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 163;BA.debugLine="MsgboxAsync(\"El servidor no devolvió los valor";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("El servidor no devolvió los valores esperados."+_job._errormessage /*String*/ ),BA.ObjectToCharSequence("Registro"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 165;BA.debugLine="MsgboxAsync(\"The server did not return the exp";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("The server did not return the expected values."+_job._errormessage /*String*/ ),BA.ObjectToCharSequence("Sign up"),processBA);
 };
 };
 };
 //BA.debugLineNum = 169;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 170;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 171;BA.debugLine="End Sub";
return "";
}
public static boolean  _validate_email(String _emailaddress) throws Exception{
anywheresoftware.b4a.keywords.Regex.MatcherWrapper _matchemail = null;
 //BA.debugLineNum = 172;BA.debugLine="Sub Validate_Email(EmailAddress As String) As Bool";
 //BA.debugLineNum = 173;BA.debugLine="Dim MatchEmail As Matcher = Regex.Matcher(\"^(?";
_matchemail = new anywheresoftware.b4a.keywords.Regex.MatcherWrapper();
_matchemail = anywheresoftware.b4a.keywords.Common.Regex.Matcher("^(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])$",_emailaddress);
 //BA.debugLineNum = 175;BA.debugLine="If MatchEmail.Find = True Then";
if (_matchemail.Find()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 177;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 180;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return false;
}
}
