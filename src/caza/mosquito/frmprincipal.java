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

public class frmprincipal extends Activity implements B4AActivity{
	public static frmprincipal mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmprincipal");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmprincipal).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmprincipal");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmprincipal", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmprincipal) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmprincipal) Resume **");
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
		return frmprincipal.class;
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
            BA.LogInfo("** Activity (frmprincipal) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmprincipal) Pause event (activity is not paused). **");
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
            frmprincipal mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmprincipal) Resume **");
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
public static String _fullidcurrentproject = "";
public static anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static String _currentscreen = "";
public static boolean _resetting_msg = false;
public anywheresoftware.b4a.objects.TabStripViewPager _tabstripmain = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmenu = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblencontremosquito = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btninformemosquito = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblencontrecriadero = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btninformecriadero = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public caza.mosquito.b4xdrawer _drawer = null;
public anywheresoftware.b4a.objects.LabelWrapper _btncomofotosreportar = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerrarsesion = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnedituser = null;
public anywheresoftware.b4a.objects.LabelWrapper _btndatosanteriores = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblusername = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblregistrate = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnverperfil = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnabout = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnpoliticadatos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgrupo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblusernamemain = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgrupomain = null;
public caza.mosquito.main _main = null;
public caza.mosquito.frmcamara _frmcamara = null;
public caza.mosquito.frmquehacer _frmquehacer = null;
public caza.mosquito.register _register = null;
public caza.mosquito.frmlogin _frmlogin = null;
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
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 58;BA.debugLine="Drawer.Initialize(Me, \"Drawer\", Activity, 300dip)";
mostCurrent._drawer._initialize /*String*/ (mostCurrent.activityBA,frmprincipal.getObject(),"Drawer",(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)));
 //BA.debugLineNum = 59;BA.debugLine="Drawer.CenterPanel.LoadLayout(\"frmPrincipal_CM2\")";
mostCurrent._drawer._getcenterpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("frmPrincipal_CM2",mostCurrent.activityBA);
 //BA.debugLineNum = 60;BA.debugLine="Drawer.LeftPanel.LoadLayout(\"frmSideNav\")";
mostCurrent._drawer._getleftpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("frmSideNav",mostCurrent.activityBA);
 //BA.debugLineNum = 62;BA.debugLine="If Main.username = \"guest\" Or Main.username = \"\"";
if ((mostCurrent._main._username /*String*/ ).equals("guest") || (mostCurrent._main._username /*String*/ ).equals("")) { 
 //BA.debugLineNum = 63;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 64;BA.debugLine="btnCerrarSesion.Text = \"Iniciar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Iniciar sesión"));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 66;BA.debugLine="btnCerrarSesion.Text = \"Start session\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Start session"));
 };
 //BA.debugLineNum = 69;BA.debugLine="lblUserName.Visible = False";
mostCurrent._lblusername.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 70;BA.debugLine="lblRegistrate.Visible = True";
mostCurrent._lblregistrate.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 71;BA.debugLine="btnEditUser.Visible = False";
mostCurrent._btnedituser.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 73;BA.debugLine="lblUserName.Text = Main.username";
mostCurrent._lblusername.setText(BA.ObjectToCharSequence(mostCurrent._main._username /*String*/ ));
 //BA.debugLineNum = 74;BA.debugLine="lblUserNameMain.Text = \"¡Hola, \" & Main.username";
mostCurrent._lblusernamemain.setText(BA.ObjectToCharSequence("¡Hola, "+mostCurrent._main._username /*String*/ +"!"));
 //BA.debugLineNum = 75;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 76;BA.debugLine="btnCerrarSesion.Text = \"Cerrar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Cerrar sesión"));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 78;BA.debugLine="btnCerrarSesion.Text = \"Close session\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Close session"));
 };
 //BA.debugLineNum = 80;BA.debugLine="lblRegistrate.Visible = False";
mostCurrent._lblregistrate.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 81;BA.debugLine="btnEditUser.Visible = True";
mostCurrent._btnedituser.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 82;BA.debugLine="If Main.strUserGroup <> \"\" Then";
if ((mostCurrent._main._strusergroup /*String*/ ).equals("") == false) { 
 //BA.debugLineNum = 83;BA.debugLine="lblGrupo.Text = Main.strUserGroup";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence(mostCurrent._main._strusergroup /*String*/ ));
 //BA.debugLineNum = 84;BA.debugLine="lblGrupoMain.text = Main.strUserGroup";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence(mostCurrent._main._strusergroup /*String*/ ));
 }else {
 //BA.debugLineNum = 86;BA.debugLine="lblGrupo.Text = \"¡Aún no trabajas en grupo!\"";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 //BA.debugLineNum = 87;BA.debugLine="lblGrupoMain.text = \"¡Aún no trabajas en grupo!";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 };
 };
 //BA.debugLineNum = 92;BA.debugLine="If frmDatosAnteriores.notificacion = True Then";
if (mostCurrent._frmdatosanteriores._notificacion /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 93;BA.debugLine="CallSubDelayed3(frmDatosAnteriores,\"VerDetalles\"";
anywheresoftware.b4a.keywords.Common.CallSubDelayed3(processBA,(Object)(mostCurrent._frmdatosanteriores.getObject()),"VerDetalles",(Object)(mostCurrent._frmdatosanteriores._serveridnum /*String*/ ),(Object)(anywheresoftware.b4a.keywords.Common.True));
 };
 //BA.debugLineNum = 97;BA.debugLine="If Main.modooffline = True Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 98;BA.debugLine="startBienvienido(False, True)";
_startbienvienido(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 99;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 101;BA.debugLine="startBienvienido(True, True)";
_startbienvienido(anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 104;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
String _msg = "";
 //BA.debugLineNum = 119;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 121;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 122;BA.debugLine="If Drawer.LeftOpen Then";
if (mostCurrent._drawer._getleftopen /*boolean*/ ()) { 
 //BA.debugLineNum = 123;BA.debugLine="Drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 124;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 126;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 127;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 128;BA.debugLine="msg = Msgbox2(\"Salir de la aplicación?\", \"SAL";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Salir de la aplicación?"),BA.ObjectToCharSequence("SALIR"),"Si, deseo salir","","No, me equivoqué",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 130;BA.debugLine="msg = Msgbox2(\"Exit the app?\", \"EXIT\", \"Yes, e";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Exit the app?"),BA.ObjectToCharSequence("EXIT"),"Yes, exit","","Nop, my bad",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 132;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 133;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 };
 //BA.debugLineNum = 136;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 138;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 116;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 107;BA.debugLine="If Main.strUserGroup <> \"\" Then";
if ((mostCurrent._main._strusergroup /*String*/ ).equals("") == false) { 
 //BA.debugLineNum = 108;BA.debugLine="lblGrupo.Text = Main.strUserGroup";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence(mostCurrent._main._strusergroup /*String*/ ));
 //BA.debugLineNum = 109;BA.debugLine="lblGrupoMain.text = Main.strUserGroup";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence(mostCurrent._main._strusergroup /*String*/ ));
 }else {
 //BA.debugLineNum = 111;BA.debugLine="lblGrupo.Text = \"¡Aún no trabajas en grupo!\"";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 //BA.debugLineNum = 112;BA.debugLine="lblGrupoMain.text = \"¡Aún no trabajas en grupo!\"";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 };
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _btnabout_click() throws Exception{
 //BA.debugLineNum = 208;BA.debugLine="Sub btnAbout_Click";
 //BA.debugLineNum = 209;BA.debugLine="StartActivity(frmabout)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmabout.getObject()));
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _btnaccion_click() throws Exception{
 //BA.debugLineNum = 519;BA.debugLine="Sub btnAccion_Click";
 //BA.debugLineNum = 520;BA.debugLine="StartActivity(frmQueHacer)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmquehacer.getObject()));
 //BA.debugLineNum = 521;BA.debugLine="End Sub";
return "";
}
public static String  _btnaprender_click() throws Exception{
 //BA.debugLineNum = 497;BA.debugLine="Sub btnAprender_Click";
 //BA.debugLineNum = 498;BA.debugLine="StartActivity(frmInfografias_Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frminfografias_main.getObject()));
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarsesion_click() throws Exception{
String _msg = "";
 //BA.debugLineNum = 187;BA.debugLine="Sub btnCerrarSesion_Click";
 //BA.debugLineNum = 188;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 189;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 190;BA.debugLine="msg = Msgbox2(\"Desea cerrar la sesión? Ingresar";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Desea cerrar la sesión? Ingresar con otro usuario requiere de internet!"),BA.ObjectToCharSequence("Seguro?"),"Si, tengo internet","","No, no tengo internet ahora",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 192;BA.debugLine="msg = Msgbox2(\"Do you want to close the session?";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do you want to close the session? To log in with another user you need an internet connection!"),BA.ObjectToCharSequence("Sure?"),"Yes, I have internet","","No, i'm offline now",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 194;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 195;BA.debugLine="Main.strUserID = \"\"";
mostCurrent._main._struserid /*String*/  = "";
 //BA.debugLineNum = 196;BA.debugLine="Main.strUserName = \"\"";
mostCurrent._main._strusername /*String*/  = "";
 //BA.debugLineNum = 197;BA.debugLine="Main.strUserLocation = \"\"";
mostCurrent._main._struserlocation /*String*/  = "";
 //BA.debugLineNum = 198;BA.debugLine="Main.strUserEmail = \"\"";
mostCurrent._main._struseremail /*String*/  = "";
 //BA.debugLineNum = 199;BA.debugLine="Main.strUserOrg = \"\"";
mostCurrent._main._struserorg /*String*/  = "";
 //BA.debugLineNum = 200;BA.debugLine="Main.username = \"\"";
mostCurrent._main._username /*String*/  = "";
 //BA.debugLineNum = 201;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 202;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 204;BA.debugLine="CallSubDelayed(frmLogin, \"SignOutGoogle\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._frmlogin.getObject()),"SignOutGoogle");
 //BA.debugLineNum = 205;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmlogin.getObject()));
 };
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _btncomofotosreportar_click() throws Exception{
 //BA.debugLineNum = 467;BA.debugLine="Sub btnComoFotosReportar_Click";
 //BA.debugLineNum = 468;BA.debugLine="StartActivity(frmComoFotos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmcomofotos.getObject()));
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static String  _btndatosanteriores_click() throws Exception{
 //BA.debugLineNum = 471;BA.debugLine="Sub btnDatosAnteriores_Click";
 //BA.debugLineNum = 473;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 474;BA.debugLine="StartActivity(frmDatosAnteriores)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmdatosanteriores.getObject()));
 }else {
 //BA.debugLineNum = 476;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 478;BA.debugLine="ToastMessageShow(\"No ha iniciado sesión aún\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No ha iniciado sesión aún"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 481;BA.debugLine="ToastMessageShow(\"You haven't logged in yet\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You haven't logged in yet"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 486;BA.debugLine="End Sub";
return "";
}
public static String  _btnedituser_click() throws Exception{
 //BA.debugLineNum = 211;BA.debugLine="Sub btnEditUser_Click";
 //BA.debugLineNum = 212;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 213;BA.debugLine="StartActivity(frmEditProfile)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmeditprofile.getObject()));
 }else {
 //BA.debugLineNum = 215;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 216;BA.debugLine="ToastMessageShow(\"Necesita tener internet para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita tener internet para editar su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 218;BA.debugLine="ToastMessageShow(\"You need to be online to chec";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be online to check your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public static String  _btninformecriadero_click() throws Exception{
 //BA.debugLineNum = 375;BA.debugLine="Sub btnInformeCriadero_Click";
 //BA.debugLineNum = 377;BA.debugLine="If resetting_msg = True Then";
if (_resetting_msg==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 378;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 379;BA.debugLine="ToastMessageShow(\"Buscando señal... aguarde uno";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Buscando señal... aguarde unos segundos y vuelva a intentarlo"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 381;BA.debugLine="ToastMessageShow(\"Looking for signal... please";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Looking for signal... please wait a few seconds and try again"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 383;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 386;BA.debugLine="ComenzarReporte(\"Criadero\")";
_comenzarreporte("Criadero");
 //BA.debugLineNum = 387;BA.debugLine="End Sub";
return "";
}
public static String  _btninformemosquito_click() throws Exception{
 //BA.debugLineNum = 358;BA.debugLine="Sub btnInformeMosquito_Click";
 //BA.debugLineNum = 360;BA.debugLine="If resetting_msg = True Then";
if (_resetting_msg==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 361;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 362;BA.debugLine="ToastMessageShow(\"Buscando señal... aguarde uno";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Buscando señal... aguarde unos segundos y vuelva a intentarlo"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 364;BA.debugLine="ToastMessageShow(\"Looking for signal... please";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Looking for signal... please wait a few seconds and try again"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 367;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 369;BA.debugLine="ComenzarReporte(\"Mosquito\")";
_comenzarreporte("Mosquito");
 //BA.debugLineNum = 370;BA.debugLine="End Sub";
return "";
}
public static String  _btnlangen_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 241;BA.debugLine="Sub btnLangEn_Click";
 //BA.debugLineNum = 242;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 243;BA.debugLine="Main.lang = \"en\"";
mostCurrent._main._lang /*String*/  = "en";
 //BA.debugLineNum = 244;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 245;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 246;BA.debugLine="Map1.Put(\"configID\", \"1\")";
_map1.Put((Object)("configID"),(Object)("1"));
 //BA.debugLineNum = 247;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"appconfig\",";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"appconfig","applang",(Object)(mostCurrent._main._lang /*String*/ ),_map1);
 //BA.debugLineNum = 249;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 250;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 251;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,frmprincipal.getObject());
 };
 //BA.debugLineNum = 254;BA.debugLine="End Sub";
return "";
}
public static String  _btnlanges_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 227;BA.debugLine="Sub btnLangEs_Click";
 //BA.debugLineNum = 228;BA.debugLine="If Main.lang = \"en\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 229;BA.debugLine="Main.lang = \"es\"";
mostCurrent._main._lang /*String*/  = "es";
 //BA.debugLineNum = 230;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 231;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 232;BA.debugLine="Map1.Put(\"configID\", \"1\")";
_map1.Put((Object)("configID"),(Object)("1"));
 //BA.debugLineNum = 233;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"appconfig\",";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"appconfig","applang",(Object)(mostCurrent._main._lang /*String*/ ),_map1);
 //BA.debugLineNum = 235;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 236;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 237;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,frmprincipal.getObject());
 };
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _btnmenu_click() throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub btnMenu_Click";
 //BA.debugLineNum = 155;BA.debugLine="Drawer.LeftOpen = True";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 156;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._main._username /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 157;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 158;BA.debugLine="btnCerrarSesion.Text = \"Iniciar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Iniciar sesión"));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 160;BA.debugLine="btnCerrarSesion.Text = \"Sign in\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Sign in"));
 };
 };
 //BA.debugLineNum = 163;BA.debugLine="lblGrupo.Text = Main.strUserGroup";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence(mostCurrent._main._strusergroup /*String*/ ));
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _btnpoliticadatos_click() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Sub btnPoliticaDatos_Click";
 //BA.debugLineNum = 224;BA.debugLine="StartActivity(frmPoliticaDatos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmpoliticadatos.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _btnverperfil_click() throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub btnVerPerfil_Click";
 //BA.debugLineNum = 167;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._main._username /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 168;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 169;BA.debugLine="ToastMessageShow(\"Necesita estar registrado par";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita estar registrado para ver su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 171;BA.debugLine="ToastMessageShow(\"You need to be registered to";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be registered to view your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 174;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 175;BA.debugLine="StartActivity(frmEditProfile)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmeditprofile.getObject()));
 }else {
 //BA.debugLineNum = 177;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 178;BA.debugLine="ToastMessageShow(\"Necesita tener internet para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita tener internet para editar su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 180;BA.debugLine="ToastMessageShow(\"You need to be online to che";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be online to check your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 };
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _butexplorarmapa_click() throws Exception{
 //BA.debugLineNum = 509;BA.debugLine="Sub butExplorarMapa_Click";
 //BA.debugLineNum = 510;BA.debugLine="StartActivity(frmMapa)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmmapa.getObject()));
 //BA.debugLineNum = 511;BA.debugLine="End Sub";
return "";
}
public static String  _comenzarreporte(String _tiporeporte) throws Exception{
anywheresoftware.b4a.objects.collections.List _neweval = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
anywheresoftware.b4a.objects.collections.Map _currentprojectmap = null;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
String _usernametemp = "";
 //BA.debugLineNum = 392;BA.debugLine="Sub ComenzarReporte(tiporeporte As String)";
 //BA.debugLineNum = 393;BA.debugLine="Main.fotopath0 = \"\"";
mostCurrent._main._fotopath0 /*String*/  = "";
 //BA.debugLineNum = 394;BA.debugLine="Main.fotopath1 = \"\"";
mostCurrent._main._fotopath1 /*String*/  = "";
 //BA.debugLineNum = 395;BA.debugLine="Main.fotopath2 = \"\"";
mostCurrent._main._fotopath2 /*String*/  = "";
 //BA.debugLineNum = 396;BA.debugLine="Main.fotopath3 = \"\"";
mostCurrent._main._fotopath3 /*String*/  = "";
 //BA.debugLineNum = 397;BA.debugLine="Main.latitud = \"\"";
mostCurrent._main._latitud /*String*/  = "";
 //BA.debugLineNum = 398;BA.debugLine="Main.longitud = \"\"";
mostCurrent._main._longitud /*String*/  = "";
 //BA.debugLineNum = 399;BA.debugLine="Main.tipoevaluacion = tiporeporte";
mostCurrent._main._tipoevaluacion /*String*/  = _tiporeporte;
 //BA.debugLineNum = 403;BA.debugLine="Dim newEval As List";
_neweval = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 404;BA.debugLine="newEval.Initialize";
_neweval.Initialize();
 //BA.debugLineNum = 405;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 406;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 407;BA.debugLine="m.Put(\"usuario\", Main.username)";
_m.Put((Object)("usuario"),(Object)(mostCurrent._main._username /*String*/ ));
 //BA.debugLineNum = 408;BA.debugLine="m.Put(\"tipoEval\", tiporeporte)";
_m.Put((Object)("tipoEval"),(Object)(_tiporeporte));
 //BA.debugLineNum = 409;BA.debugLine="newEval.Add(m)";
_neweval.Add((Object)(_m.getObject()));
 //BA.debugLineNum = 410;BA.debugLine="DBUtils.InsertMaps(Starter.sqlDB,\"markers_local\",";
mostCurrent._dbutils._insertmaps /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local",_neweval);
 //BA.debugLineNum = 418;BA.debugLine="Dim currentprojectMap As Map";
_currentprojectmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 419;BA.debugLine="currentprojectMap.Initialize";
_currentprojectmap.Initialize();
 //BA.debugLineNum = 420;BA.debugLine="Try";
try { //BA.debugLineNum = 421;BA.debugLine="currentprojectMap = DBUtils.ExecuteMap(Starter.s";
_currentprojectmap = mostCurrent._dbutils._executemap /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local ORDER BY id DESC LIMIT 1",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 422;BA.debugLine="If currentprojectMap = Null Or currentprojectMap";
if (_currentprojectmap== null || _currentprojectmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 424;BA.debugLine="Main.currentproject = 1";
mostCurrent._main._currentproject /*String*/  = BA.NumberToString(1);
 //BA.debugLineNum = 425;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 426;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 427;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 428;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 429;BA.debugLine="Main.datecurrentproject = DateTime.Date(DateTim";
mostCurrent._main._datecurrentproject /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 430;BA.debugLine="Dim usernameTemp As String";
_usernametemp = "";
 //BA.debugLineNum = 431;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._main._username /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 432;BA.debugLine="usernameTemp = \"guest\" & Main.deviceID.SubStri";
_usernametemp = "guest"+mostCurrent._main._deviceid /*String*/ .substring((int) (0),(int) (6));
 }else {
 //BA.debugLineNum = 434;BA.debugLine="usernameTemp = Main.username";
_usernametemp = mostCurrent._main._username /*String*/ ;
 };
 //BA.debugLineNum = 436;BA.debugLine="fullidcurrentproject = usernameTemp & \"_\" & Mai";
_fullidcurrentproject = _usernametemp+"_"+mostCurrent._main._currentproject /*String*/ +"_"+mostCurrent._main._datecurrentproject /*String*/ ;
 //BA.debugLineNum = 437;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","fullID",(Object)(_fullidcurrentproject),_map1);
 }else {
 //BA.debugLineNum = 439;BA.debugLine="Main.currentproject = currentprojectMap.Get(\"id";
mostCurrent._main._currentproject /*String*/  = BA.ObjectToString(_currentprojectmap.Get((Object)("id")));
 //BA.debugLineNum = 440;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 441;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 442;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 443;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 444;BA.debugLine="Main.datecurrentproject = DateTime.Date(DateTim";
mostCurrent._main._datecurrentproject /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 445;BA.debugLine="Dim usernameTemp As String";
_usernametemp = "";
 //BA.debugLineNum = 446;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._main._username /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 447;BA.debugLine="usernameTemp = \"guest\" & Main.deviceID.SubStri";
_usernametemp = "guest"+mostCurrent._main._deviceid /*String*/ .substring((int) (0),(int) (6));
 }else {
 //BA.debugLineNum = 449;BA.debugLine="usernameTemp = Main.username";
_usernametemp = mostCurrent._main._username /*String*/ ;
 };
 //BA.debugLineNum = 451;BA.debugLine="fullidcurrentproject = usernameTemp & \"_\" & Mai";
_fullidcurrentproject = _usernametemp+"_"+mostCurrent._main._currentproject /*String*/ +"_"+mostCurrent._main._datecurrentproject /*String*/ ;
 //BA.debugLineNum = 452;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._dbutils._updaterecord /*String*/ (mostCurrent.activityBA,mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","fullID",(Object)(_fullidcurrentproject),_map1);
 };
 } 
       catch (Exception e52) {
			processBA.setLastException(e52); //BA.debugLineNum = 457;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("46750273",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 458;BA.debugLine="ToastMessageShow(\"Hubo un error, intente de nuev";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Hubo un error, intente de nuevo"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 459;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 463;BA.debugLine="Main.Idproyecto = Main.currentproject";
mostCurrent._main._idproyecto /*int*/  = (int)(Double.parseDouble(mostCurrent._main._currentproject /*String*/ ));
 //BA.debugLineNum = 464;BA.debugLine="StartActivity(frmInstrucciones)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frminstrucciones.getObject()));
 //BA.debugLineNum = 466;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim currentscreen As String";
mostCurrent._currentscreen = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim resetting_msg As Boolean";
_resetting_msg = false;
 //BA.debugLineNum = 21;BA.debugLine="Private tabStripMain As TabStrip";
mostCurrent._tabstripmain = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 22;BA.debugLine="Private btnMenu As Button";
mostCurrent._btnmenu = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblEncontreMosquito As Label";
mostCurrent._lblencontremosquito = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private btnInformeMosquito As Button";
mostCurrent._btninformemosquito = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lblEncontreCriadero As Label";
mostCurrent._lblencontrecriadero = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private btnInformeCriadero As Button";
mostCurrent._btninformecriadero = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 34;BA.debugLine="Dim Drawer As B4XDrawer";
mostCurrent._drawer = new caza.mosquito.b4xdrawer();
 //BA.debugLineNum = 35;BA.debugLine="Private btnComoFotosReportar As Label";
mostCurrent._btncomofotosreportar = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private btnCerrarSesion As Button";
mostCurrent._btncerrarsesion = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private btnEditUser As Label";
mostCurrent._btnedituser = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private btnDatosAnteriores As Label";
mostCurrent._btndatosanteriores = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblUserName As Label";
mostCurrent._lblusername = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblRegistrate As Label";
mostCurrent._lblregistrate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btnVerPerfil As Label";
mostCurrent._btnverperfil = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private btnAbout As Label";
mostCurrent._btnabout = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private btnPoliticaDatos As Label";
mostCurrent._btnpoliticadatos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private lblGrupo As Label";
mostCurrent._lblgrupo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private lblUserNameMain As Label";
mostCurrent._lblusernamemain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lblGrupoMain As Label";
mostCurrent._lblgrupomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _imglogo_click() throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub imgLogo_Click";
 //BA.debugLineNum = 151;BA.debugLine="StartActivity(frmabout)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmabout.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _lblaccion_click() throws Exception{
 //BA.debugLineNum = 523;BA.debugLine="Sub lblAccion_Click";
 //BA.debugLineNum = 524;BA.debugLine="btnAccion_Click";
_btnaccion_click();
 //BA.debugLineNum = 525;BA.debugLine="End Sub";
return "";
}
public static String  _lblaprender_click() throws Exception{
 //BA.debugLineNum = 501;BA.debugLine="Sub lblAprender_Click";
 //BA.debugLineNum = 502;BA.debugLine="StartActivity(frmInfografias_Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frminfografias_main.getObject()));
 //BA.debugLineNum = 503;BA.debugLine="End Sub";
return "";
}
public static String  _lblexplorarmapa_click() throws Exception{
 //BA.debugLineNum = 512;BA.debugLine="Sub lblExplorarMapa_Click";
 //BA.debugLineNum = 513;BA.debugLine="StartActivity(frmMapa)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmmapa.getObject()));
 //BA.debugLineNum = 514;BA.debugLine="End Sub";
return "";
}
public static String  _lblgrupo_click() throws Exception{
 //BA.debugLineNum = 580;BA.debugLine="Sub lblGrupo_Click";
 //BA.debugLineNum = 581;BA.debugLine="MsgboxAsync(\"¡Puedes formar parte de un grupo o c";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("¡Puedes formar parte de un grupo o colectivo de personas para coordinar tus esfuerzos!"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Unite a un grupo desde 'Mi perfil', en el menú lateral"),BA.ObjectToCharSequence("Unite a un grupo!"),processBA);
 //BA.debugLineNum = 582;BA.debugLine="End Sub";
return "";
}
public static String  _lblgrupomain_click() throws Exception{
 //BA.debugLineNum = 584;BA.debugLine="Sub lblGrupoMain_Click";
 //BA.debugLineNum = 585;BA.debugLine="MsgboxAsync(\"¡Puedes formar parte de un grupo o c";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("¡Puedes formar parte de un grupo o colectivo de personas para coordinar tus esfuerzos!"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Unite a un grupo desde 'Mi perfil', en el menú lateral"),BA.ObjectToCharSequence("Unite a un grupo!"),processBA);
 //BA.debugLineNum = 586;BA.debugLine="End Sub";
return "";
}
public static String  _lblinformecriadero_click() throws Exception{
 //BA.debugLineNum = 388;BA.debugLine="Sub lblInformeCriadero_Click";
 //BA.debugLineNum = 389;BA.debugLine="btnInformeCriadero_Click";
_btninformecriadero_click();
 //BA.debugLineNum = 390;BA.debugLine="End Sub";
return "";
}
public static String  _lblinformemosquito_click() throws Exception{
 //BA.debugLineNum = 371;BA.debugLine="Sub lblInformeMosquito_Click";
 //BA.debugLineNum = 372;BA.debugLine="btnInformeMosquito_Click";
_btninformemosquito_click();
 //BA.debugLineNum = 373;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim fullidcurrentproject As String";
_fullidcurrentproject = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _resetmessages() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 537;BA.debugLine="Sub ResetMessages";
 //BA.debugLineNum = 540;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 541;BA.debugLine="dd.url = Main.serverPath & \"/connect2/resetmessag";
_dd.url /*String*/  = mostCurrent._main._serverpath /*String*/ +"/connect2/resetmessages_new.php?deviceID="+mostCurrent._main._deviceid /*String*/ ;
 //BA.debugLineNum = 542;BA.debugLine="dd.EventName = \"ResetMessages\"";
_dd.EventName /*String*/  = "ResetMessages";
 //BA.debugLineNum = 543;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmprincipal.getObject();
 //BA.debugLineNum = 544;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._downloadservice.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 545;BA.debugLine="resetting_msg = True";
_resetting_msg = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 546;BA.debugLine="End Sub";
return "";
}
public static String  _resetmessages_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
 //BA.debugLineNum = 548;BA.debugLine="Sub ResetMessages_Complete(Job As HttpJob)";
 //BA.debugLineNum = 549;BA.debugLine="Log(\"Reset messages: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("47405569","Reset messages: "+BA.ObjectToString(_job._success /*boolean*/ ),0);
 //BA.debugLineNum = 550;BA.debugLine="If Job.Success = True Then";
if (_job._success /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 551;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 552;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 553;BA.debugLine="ret = Job.GetString";
_ret = _job._getstring /*String*/ ();
 //BA.debugLineNum = 554;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 555;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 556;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 557;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 558;BA.debugLine="ToastMessageShow(\"Error\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("Error reseteando los mensajes, ")) { 
 //BA.debugLineNum = 560;BA.debugLine="ToastMessageShow(\"Login failed\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Login failed"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("ResetMessages")) { 
 };
 //BA.debugLineNum = 564;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 566;BA.debugLine="Log(\"reset messages not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("47405586","reset messages not ok",0);
 //BA.debugLineNum = 567;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 568;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 570;BA.debugLine="MsgboxAsync(\"There seems to be a problem with t";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with the server, we will fix it soon!"),BA.ObjectToCharSequence("My bad!"),processBA);
 };
 //BA.debugLineNum = 572;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 575;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 576;BA.debugLine="End Sub";
return "";
}
public static String  _startbienvienido(boolean _online,boolean _firsttime) throws Exception{
String _msjpriv = "";
String[] _msjarray = null;
int _i = 0;
boolean _hayevals = false;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _msg = "";
 //BA.debugLineNum = 264;BA.debugLine="Sub startBienvienido(online As Boolean, firsttime";
 //BA.debugLineNum = 267;BA.debugLine="If online = True Then";
if (_online==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 271;BA.debugLine="If Main.msjprivadoleido = True Then";
if (mostCurrent._main._msjprivadoleido /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 272;BA.debugLine="ResetMessages";
_resetmessages();
 };
 //BA.debugLineNum = 276;BA.debugLine="If Main.msjprivadouser <> \"None\" And firsttime";
if ((mostCurrent._main._msjprivadouser /*String*/ ).equals("None") == false && _firsttime==anywheresoftware.b4a.keywords.Common.True && mostCurrent._main._msjprivadoleido /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 277;BA.debugLine="Dim msjpriv As String";
_msjpriv = "";
 //BA.debugLineNum = 279;BA.debugLine="Dim msjarray() As String = Regex.Split(\"#\", Ma";
_msjarray = anywheresoftware.b4a.keywords.Common.Regex.Split("#",mostCurrent._main._msjprivadouser /*String*/ );
 //BA.debugLineNum = 280;BA.debugLine="For i = 0 To msjarray.Length - 1";
{
final int step8 = 1;
final int limit8 = (int) (_msjarray.length-1);
_i = (int) (0) ;
for (;_i <= limit8 ;_i = _i + step8 ) {
 //BA.debugLineNum = 282;BA.debugLine="If msjarray(i) <> \"\" Then";
if ((_msjarray[_i]).equals("") == false) { 
 //BA.debugLineNum = 284;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 285;BA.debugLine="msjpriv = utilidades.Mensaje(\"Notificación\",";
_msjpriv = mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Notificación","MsgMensaje.png","Notificación privada",_msjarray[_i],"Ok","","",anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 287;BA.debugLine="msjpriv = utilidades.Mensaje(\"Notification\",";
_msjpriv = mostCurrent._utilidades._mensaje /*String*/ (mostCurrent.activityBA,"Notification","MsgMensaje.png","Private notification",_msjarray[_i],"Ok","","",anywheresoftware.b4a.keywords.Common.True);
 };
 };
 }
};
 //BA.debugLineNum = 295;BA.debugLine="If msjpriv = DialogResponse.POSITIVE Then";
if ((_msjpriv).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 };
 };
 //BA.debugLineNum = 308;BA.debugLine="Dim hayevals As Boolean";
_hayevals = false;
 //BA.debugLineNum = 309;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 310;BA.debugLine="Cursor1 = Starter.SQLdb.ExecQuery(\"SELECT * FROM";
_cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(mostCurrent._starter._sqldb /*anywheresoftware.b4a.sql.SQL*/ .ExecQuery("SELECT * FROM markers_local WHERE usuario='"+mostCurrent._main._username /*String*/ +"' AND evalsent='no'")));
 //BA.debugLineNum = 311;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 312;BA.debugLine="hayevals = True";
_hayevals = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 314;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 318;BA.debugLine="If hayevals = True And firsttime = True Then";
if (_hayevals==anywheresoftware.b4a.keywords.Common.True && _firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 320;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 321;BA.debugLine="Dim msg As String = Msgbox2(\"Tiene reportes s";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Tiene reportes sin enviar. Desea hacerlo ahora?"),BA.ObjectToCharSequence("Atención"),"Si, los enviaré ahora","","No, las enviaré luego",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 323;BA.debugLine="Dim msg As String = Msgbox2(\"You have unsent";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("You have unsent reports, do you want to send them now?"),BA.ObjectToCharSequence("Warning"),"Yes, I'll send them now","","No, I'll send them later",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 327;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 328;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._main._modooffline /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 329;BA.debugLine="frmDatosAnteriores.hayAnteriores = True";
mostCurrent._frmdatosanteriores._hayanteriores /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 330;BA.debugLine="StartActivity(frmDatosAnteriores)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frmdatosanteriores.getObject()));
 }else {
 //BA.debugLineNum = 332;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._main._lang /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 333;BA.debugLine="ToastMessageShow(\"No ha iniciado sesión aún";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No ha iniciado sesión aún"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._main._lang /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 335;BA.debugLine="ToastMessageShow(\"You haven't logged in yet";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You haven't logged in yet"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 };
 };
 }else {
 //BA.debugLineNum = 344;BA.debugLine="Main.modooffline = True";
mostCurrent._main._modooffline /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 345;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
}
