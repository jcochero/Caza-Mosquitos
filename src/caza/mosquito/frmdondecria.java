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

public class frmdondecria extends Activity implements B4AActivity{
	public static frmdondecria mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmdondecria");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmdondecria).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmdondecria");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmdondecria", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmdondecria) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmdondecria) Resume **");
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
		return frmdondecria.class;
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
            BA.LogInfo("** Activity (frmdondecria) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmdondecria) Pause event (activity is not paused). **");
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
            frmdondecria mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmdondecria) Resume **");
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
public anywheresoftware.b4a.objects.LabelWrapper _lbltitulomain = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsubtitulomain = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scrollbotdondecria = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblinformacionmain = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblarrow = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblinformacionrojo = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btninformacionextra = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbalde = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btngota = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnpersona = null;
public anywheresoftware.b4a.objects.ButtonWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnzanjas = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcaract_fondo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcaract_top = null;
public anywheresoftware.b4a.objects.ButtonWrapper _but_backcaract = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltitulo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcar1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcar2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcar3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfondo = null;
public anywheresoftware.b4a.objects.PanelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcheck3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcheck2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcheck1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbebedero = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnfrasco = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnneumatico = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntanque = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmaceta = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncanaleta = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scrollejemplos = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgejemplomain = null;
public anywheresoftware.b4a.objects.ButtonWrapper _but_backejemplos = null;
public anywheresoftware.b4a.objects.ButtonWrapper _but_cerrar = null;
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
 //BA.debugLineNum = 60;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 62;BA.debugLine="Activity.LoadLayout(\"lay_dondeCria_Main\")";
mostCurrent._activity.LoadLayout("lay_dondeCria_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 65;BA.debugLine="scrollBotDondeCria.Panel.LoadLayout(\"lay_dondeCri";
mostCurrent._scrollbotdondecria.getPanel().LoadLayout("lay_dondeCria_Paneles",mostCurrent.activityBA);
 //BA.debugLineNum = 66;BA.debugLine="scrollBotDondeCria.Panel.Width = 600dip";
mostCurrent._scrollbotdondecria.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (600)));
 //BA.debugLineNum = 67;BA.debugLine="scrollBotDondeCria.FullScroll(True)";
mostCurrent._scrollbotdondecria.FullScroll(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 69;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 71;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _btnbalde_click() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub btnBalde_Click";
 //BA.debugLineNum = 121;BA.debugLine="btnInformacionExtra.Visible = True";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="lblInformacionMain.Visible = True";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 123;BA.debugLine="lblInformacionRojo.Visible = True";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 124;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 125;BA.debugLine="lblArrow.Left = (btnBalde.Left + btnBalde.Width/2";
mostCurrent._lblarrow.setLeft((int) ((mostCurrent._btnbalde.getLeft()+mostCurrent._btnbalde.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)))-mostCurrent._scrollbotdondecria.getScrollPosition()));
 //BA.debugLineNum = 126;BA.debugLine="lblInformacionMain.Height = 180dip";
mostCurrent._lblinformacionmain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180)));
 //BA.debugLineNum = 127;BA.debugLine="lblInformacionMain.Text = CRLF & \"Puede vivir en";
mostCurrent._lblinformacionmain.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+"Puede vivir en recipientes de paredes rígidas con agua"));
 //BA.debugLineNum = 128;BA.debugLine="lblInformacionRojo.Text = \"¿Qué criaderos existir";
mostCurrent._lblinformacionrojo.setText(BA.ObjectToCharSequence("¿Qué criaderos existirán en tu casa o barrio?"));
 //BA.debugLineNum = 129;BA.debugLine="lblInformacionRojo.Top = 410dip";
mostCurrent._lblinformacionrojo.setTop(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (410)));
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _btnbebedero_click() throws Exception{
 //BA.debugLineNum = 278;BA.debugLine="Sub btnBebedero_Click";
 //BA.debugLineNum = 279;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Bebedero.png").getObject()));
 //BA.debugLineNum = 280;BA.debugLine="lblCar1.Text = \"Bebederos\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Bebederos"));
 //BA.debugLineNum = 281;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 282;BA.debugLine="imgEjemploMain.Width = 100dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 283;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 50dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
return "";
}
public static String  _btncanaleta_click() throws Exception{
 //BA.debugLineNum = 238;BA.debugLine="Sub btnCanaleta_Click";
 //BA.debugLineNum = 239;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Canaleta.png").getObject()));
 //BA.debugLineNum = 240;BA.debugLine="lblCar1.Text = \"Canaletas\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Canaletas"));
 //BA.debugLineNum = 241;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 242;BA.debugLine="imgEjemploMain.Width = 120dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120)));
 //BA.debugLineNum = 243;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 60dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))));
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _btncaracteristicas_click() throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub btnCaracteristicas_Click";
 //BA.debugLineNum = 167;BA.debugLine="lblTituloMain.Visible = False";
mostCurrent._lbltitulomain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 168;BA.debugLine="lblSubTituloMain.Visible = False";
mostCurrent._lblsubtitulomain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 169;BA.debugLine="scrollBotDondeCria.Visible  = False";
mostCurrent._scrollbotdondecria.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 170;BA.debugLine="lblCirculoPos1.Visible = False";
mostCurrent._lblcirculopos1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="lblCirculoPos2.Visible = False";
mostCurrent._lblcirculopos2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 172;BA.debugLine="lblCirculoPos3.Visible = False";
mostCurrent._lblcirculopos3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 173;BA.debugLine="lblCirculoPos4.Visible = False";
mostCurrent._lblcirculopos4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 174;BA.debugLine="lblCirculoPos5.Visible = False";
mostCurrent._lblcirculopos5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 175;BA.debugLine="lblInformacionMain.Visible = False";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 176;BA.debugLine="lblArrow.Visible = False";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 177;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 178;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 179;BA.debugLine="panelCaract.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 180;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 181;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 182;BA.debugLine="Activity.AddView(panelCaract, 0,0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 183;BA.debugLine="panelCaract.LoadLayout(\"lay_dondeCria_Caract\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.LoadLayout("lay_dondeCria_Caract",mostCurrent.activityBA);
 //BA.debugLineNum = 184;BA.debugLine="lblCheck1.Visible = True";
mostCurrent._lblcheck1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 185;BA.debugLine="lblCheck2.Visible = True";
mostCurrent._lblcheck2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 186;BA.debugLine="lblCheck3.Visible = True";
mostCurrent._lblcheck3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 187;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 188;BA.debugLine="lblCar1.Visible = False";
mostCurrent._lblcar1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 189;BA.debugLine="lblCar2.Visible = False";
mostCurrent._lblcar2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 190;BA.debugLine="lblCar3.Visible = False";
mostCurrent._lblcar3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _btnfrasco_click() throws Exception{
 //BA.debugLineNum = 270;BA.debugLine="Sub btnFrasco_Click";
 //BA.debugLineNum = 271;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Botella.png").getObject()));
 //BA.debugLineNum = 272;BA.debugLine="lblCar1.Text = \"Frascos\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Frascos"));
 //BA.debugLineNum = 273;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 274;BA.debugLine="imgEjemploMain.Width = 50dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 275;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 25dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25))));
 //BA.debugLineNum = 276;BA.debugLine="End Sub";
return "";
}
public static String  _btngota_click() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub btnGota_Click";
 //BA.debugLineNum = 132;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 133;BA.debugLine="lblInformacionMain.Visible = True";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 134;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 135;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 136;BA.debugLine="lblArrow.Left = (btnGota.Left + btnGota.Width/2 -";
mostCurrent._lblarrow.setLeft((int) ((mostCurrent._btngota.getLeft()+mostCurrent._btngota.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)))-mostCurrent._scrollbotdondecria.getScrollPosition()));
 //BA.debugLineNum = 137;BA.debugLine="lblInformacionMain.Text = CRLF & \"Las larvas pued";
mostCurrent._lblinformacionmain.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+"Las larvas puede vivir en lugares con agua limpia o sucia"));
 //BA.debugLineNum = 138;BA.debugLine="lblInformacionMain.Height = 90dip";
mostCurrent._lblinformacionmain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _btninformacionextra_click() throws Exception{
 //BA.debugLineNum = 214;BA.debugLine="Sub btnInformacionExtra_Click";
 //BA.debugLineNum = 215;BA.debugLine="lblTituloMain.Visible = False";
mostCurrent._lbltitulomain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 216;BA.debugLine="scrollBotDondeCria.Visible  = False";
mostCurrent._scrollbotdondecria.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="lblCirculoPos1.Visible = False";
mostCurrent._lblcirculopos1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 218;BA.debugLine="lblCirculoPos2.Visible = False";
mostCurrent._lblcirculopos2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 219;BA.debugLine="lblCirculoPos3.Visible = False";
mostCurrent._lblcirculopos3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="lblCirculoPos4.Visible = False";
mostCurrent._lblcirculopos4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 221;BA.debugLine="lblCirculoPos5.Visible = False";
mostCurrent._lblcirculopos5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 222;BA.debugLine="lblInformacionMain.Visible = False";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="lblArrow.Visible = False";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 224;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 225;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 226;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 227;BA.debugLine="panelCaract.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 228;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 229;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 230;BA.debugLine="Activity.AddView(panelCaract, 0,0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 231;BA.debugLine="panelCaract.LoadLayout(\"lay_dondeCria_Ejemplos\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.LoadLayout("lay_dondeCria_Ejemplos",mostCurrent.activityBA);
 //BA.debugLineNum = 232;BA.debugLine="lblCar1.Visible = True";
mostCurrent._lblcar1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 233;BA.debugLine="lblCar2.Visible = True";
mostCurrent._lblcar2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 234;BA.debugLine="scrollEjemplos.Panel.LoadLayout(\"lay_dondeCria_Ej";
mostCurrent._scrollejemplos.getPanel().LoadLayout("lay_dondeCria_EjemplosPanel",mostCurrent.activityBA);
 //BA.debugLineNum = 235;BA.debugLine="scrollEjemplos.Panel.Width = 800dip";
mostCurrent._scrollejemplos.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (800)));
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static String  _btnmaceta_click() throws Exception{
 //BA.debugLineNum = 246;BA.debugLine="Sub btnMaceta_Click";
 //BA.debugLineNum = 247;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Maceta.png").getObject()));
 //BA.debugLineNum = 248;BA.debugLine="lblCar1.Text = \"Macetas\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Macetas"));
 //BA.debugLineNum = 249;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 250;BA.debugLine="imgEjemploMain.Width = 80dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 251;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 40dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 //BA.debugLineNum = 252;BA.debugLine="End Sub";
return "";
}
public static String  _btnneumatico_click() throws Exception{
 //BA.debugLineNum = 262;BA.debugLine="Sub btnNeumatico_Click";
 //BA.debugLineNum = 263;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Neumatico.png").getObject()));
 //BA.debugLineNum = 264;BA.debugLine="lblCar1.Text = \"Neumáticos\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Neumáticos"));
 //BA.debugLineNum = 265;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 266;BA.debugLine="imgEjemploMain.Width = 80dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 267;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 40dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 //BA.debugLineNum = 268;BA.debugLine="End Sub";
return "";
}
public static String  _btnpersona_click() throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub btnPersona_Click";
 //BA.debugLineNum = 141;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="lblInformacionMain.Visible = True";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 143;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 145;BA.debugLine="lblArrow.Left = (btnPersona.Left + btnPersona.Wid";
mostCurrent._lblarrow.setLeft((int) ((mostCurrent._btnpersona.getLeft()+mostCurrent._btnpersona.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)))-mostCurrent._scrollbotdondecria.getScrollPosition()));
 //BA.debugLineNum = 146;BA.debugLine="lblInformacionMain.Height = 90dip";
mostCurrent._lblinformacionmain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 148;BA.debugLine="lblInformacionMain.Text = CRLF & \"La especie Aede";
mostCurrent._lblinformacionmain.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+"La especie Aedes aegypti se adaptó en lugares donde vive gente"));
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static String  _btntanque_click() throws Exception{
 //BA.debugLineNum = 254;BA.debugLine="Sub btnTanque_Click";
 //BA.debugLineNum = 255;BA.debugLine="imgEjemploMain.Bitmap = LoadBitmap(File.DirAssets";
mostCurrent._imgejemplomain.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"dondeCria_Tanque.png").getObject()));
 //BA.debugLineNum = 256;BA.debugLine="lblCar1.Text = \"Tanques de agua\"";
mostCurrent._lblcar1.setText(BA.ObjectToCharSequence("Tanques de agua"));
 //BA.debugLineNum = 257;BA.debugLine="imgEjemploMain.Height = 80dip";
mostCurrent._imgejemplomain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 258;BA.debugLine="imgEjemploMain.Width = 80dip";
mostCurrent._imgejemplomain.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 259;BA.debugLine="imgEjemploMain.Left = Activity.Width /2 - 40dip";
mostCurrent._imgejemplomain.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 //BA.debugLineNum = 260;BA.debugLine="End Sub";
return "";
}
public static String  _btnzanjas_click() throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Sub btnZanjas_Click";
 //BA.debugLineNum = 153;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 154;BA.debugLine="lblInformacionMain.Visible = True";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 155;BA.debugLine="lblInformacionRojo.Visible = True";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 156;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 157;BA.debugLine="lblArrow.Left = (btnZanjas.Left + btnZanjas.Width";
mostCurrent._lblarrow.setLeft((int) ((mostCurrent._btnzanjas.getLeft()+mostCurrent._btnzanjas.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)))-mostCurrent._scrollbotdondecria.getScrollPosition()));
 //BA.debugLineNum = 158;BA.debugLine="lblInformacionMain.Height = 170dip";
mostCurrent._lblinformacionmain.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 159;BA.debugLine="lblInformacionMain.Text = CRLF & \"En las zanjas,";
mostCurrent._lblinformacionmain.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.CRLF+"En las zanjas, cunetas o lagunas u otros lugares que no cumplen con estas condiciones, el mosquito no se cría"));
 //BA.debugLineNum = 160;BA.debugLine="lblInformacionRojo.Text = \"Sin embargo, pueden cr";
mostCurrent._lblinformacionrojo.setText(BA.ObjectToCharSequence("Sin embargo, pueden criarse otras especies de mosquitos, lo que sigue siendo un problema a resolver"));
 //BA.debugLineNum = 161;BA.debugLine="lblInformacionRojo.Top = 460dip";
mostCurrent._lblinformacionrojo.setTop(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (460)));
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return "";
}
public static String  _but_backcaract_click() throws Exception{
 //BA.debugLineNum = 194;BA.debugLine="Sub but_BackCaract_Click";
 //BA.debugLineNum = 195;BA.debugLine="panelCaract.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.RemoveView();
 //BA.debugLineNum = 196;BA.debugLine="lblTituloMain.Visible = True";
mostCurrent._lbltitulomain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 197;BA.debugLine="lblSubTituloMain.Visible = True";
mostCurrent._lblsubtitulomain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 198;BA.debugLine="scrollBotDondeCria.Visible  = True";
mostCurrent._scrollbotdondecria.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 199;BA.debugLine="lblCirculoPos1.Visible = True";
mostCurrent._lblcirculopos1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 200;BA.debugLine="lblCirculoPos2.Visible = True";
mostCurrent._lblcirculopos2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 201;BA.debugLine="lblCirculoPos3.Visible = True";
mostCurrent._lblcirculopos3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 202;BA.debugLine="lblCirculoPos4.Visible = True";
mostCurrent._lblcirculopos4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 203;BA.debugLine="lblCirculoPos5.Visible = True";
mostCurrent._lblcirculopos5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 204;BA.debugLine="lblInformacionMain.Visible = False";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="lblArrow.Visible = False";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 206;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 207;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="but_Cerrar.Visible = True";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 209;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _but_backejemplos_click() throws Exception{
 //BA.debugLineNum = 286;BA.debugLine="Sub but_BackEjemplos_Click";
 //BA.debugLineNum = 287;BA.debugLine="panelCaract.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.RemoveView();
 //BA.debugLineNum = 288;BA.debugLine="lblTituloMain.Visible = True";
mostCurrent._lbltitulomain.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 289;BA.debugLine="scrollBotDondeCria.Visible  = True";
mostCurrent._scrollbotdondecria.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 290;BA.debugLine="lblCirculoPos1.Visible = True";
mostCurrent._lblcirculopos1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 291;BA.debugLine="lblCirculoPos2.Visible = True";
mostCurrent._lblcirculopos2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 292;BA.debugLine="lblCirculoPos3.Visible = True";
mostCurrent._lblcirculopos3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 293;BA.debugLine="lblCirculoPos4.Visible = True";
mostCurrent._lblcirculopos4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 294;BA.debugLine="lblCirculoPos5.Visible = True";
mostCurrent._lblcirculopos5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 295;BA.debugLine="but_Cerrar.Visible = True";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 296;BA.debugLine="lblInformacionMain.Visible = False";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 297;BA.debugLine="lblArrow.Visible = False";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 298;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 299;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 300;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_click() throws Exception{
 //BA.debugLineNum = 305;BA.debugLine="Sub but_Cerrar_Click";
 //BA.debugLineNum = 306;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 307;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private lblTituloMain As Label";
mostCurrent._lbltitulomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private lblSubTituloMain As Label";
mostCurrent._lblsubtitulomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private scrollBotDondeCria As HorizontalScrollVie";
mostCurrent._scrollbotdondecria = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private lblCirculoPos1 As Label";
mostCurrent._lblcirculopos1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private lblCirculoPos2 As Label";
mostCurrent._lblcirculopos2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private lblCirculoPos3 As Label";
mostCurrent._lblcirculopos3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private lblCirculoPos4 As Label";
mostCurrent._lblcirculopos4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private lblCirculoPos5 As Label";
mostCurrent._lblcirculopos5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblInformacionMain As Label";
mostCurrent._lblinformacionmain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblArrow As Label";
mostCurrent._lblarrow = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblInformacionRojo As Label";
mostCurrent._lblinformacionrojo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private btnInformacionExtra As Button";
mostCurrent._btninformacionextra = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private btnBalde As Button";
mostCurrent._btnbalde = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private btnGota As Button";
mostCurrent._btngota = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private btnPersona As Button";
mostCurrent._btnpersona = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private btSpray As Button";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private btnZanjas As Button";
mostCurrent._btnzanjas = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private lblCaract_fondo As Label";
mostCurrent._lblcaract_fondo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private lblCaract_Top As Label";
mostCurrent._lblcaract_top = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private but_BackCaract As Button";
mostCurrent._but_backcaract = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lblTitulo As Label";
mostCurrent._lbltitulo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private lblCar1 As Label";
mostCurrent._lblcar1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblCar2 As Label";
mostCurrent._lblcar2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblCar3 As Label";
mostCurrent._lblcar3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lblFondo As Label";
mostCurrent._lblfondo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private panelCaract As Panel";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lblCheck3 As Label";
mostCurrent._lblcheck3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblCheck2 As Label";
mostCurrent._lblcheck2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private lblCheck1 As Label";
mostCurrent._lblcheck1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private btnBebedero As Button";
mostCurrent._btnbebedero = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private btnFrasco As Button";
mostCurrent._btnfrasco = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private btnNeumatico As Button";
mostCurrent._btnneumatico = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private btnTanque As Button";
mostCurrent._btntanque = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private btnMaceta As Button";
mostCurrent._btnmaceta = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private btnCanaleta As Button";
mostCurrent._btncanaleta = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private scrollEjemplos As HorizontalScrollView";
mostCurrent._scrollejemplos = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private imgEjemploMain As ImageView";
mostCurrent._imgejemplomain = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private but_BackEjemplos As Button";
mostCurrent._but_backejemplos = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private but_Cerrar As Button";
mostCurrent._but_cerrar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _scrollbotdondecria_scrollchanged(int _position) throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Sub scrollBotDondeCria_ScrollChanged(Position As I";
 //BA.debugLineNum = 83;BA.debugLine="btnInformacionExtra.Visible = False";
mostCurrent._btninformacionextra.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 84;BA.debugLine="lblInformacionMain.Visible = False";
mostCurrent._lblinformacionmain.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 85;BA.debugLine="lblInformacionRojo.Visible = False";
mostCurrent._lblinformacionrojo.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 86;BA.debugLine="lblArrow.Visible = False";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="If Position < 320 Then";
if (_position<320) { 
 //BA.debugLineNum = 88;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255,150,200,2";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (150),(int) (200),(int) (250)));
 //BA.debugLineNum = 89;BA.debugLine="lblCirculoPos2.Color = Colors.white";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 90;BA.debugLine="lblCirculoPos3.Color = Colors.white";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 91;BA.debugLine="lblCirculoPos4.Color = Colors.white";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 92;BA.debugLine="lblCirculoPos5.Color = Colors.white";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 }else if(_position>320 && _position<600) { 
 //BA.debugLineNum = 94;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255,150,200,2";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (150),(int) (200),(int) (250)));
 //BA.debugLineNum = 95;BA.debugLine="lblCirculoPos1.Color = Colors.white";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 96;BA.debugLine="lblCirculoPos3.Color = Colors.white";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 97;BA.debugLine="lblCirculoPos4.Color = Colors.white";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 98;BA.debugLine="lblCirculoPos5.Color = Colors.white";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 }else if(_position>730 && _position<1000) { 
 //BA.debugLineNum = 100;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255,150,200,2";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (150),(int) (200),(int) (250)));
 //BA.debugLineNum = 101;BA.debugLine="lblCirculoPos2.Color = Colors.white";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 102;BA.debugLine="lblCirculoPos1.Color = Colors.white";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 103;BA.debugLine="lblCirculoPos4.Color = Colors.white";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 104;BA.debugLine="lblCirculoPos5.Color = Colors.white";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 }else if(_position>1000 && _position<1320) { 
 //BA.debugLineNum = 106;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255,150,200,2";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (150),(int) (200),(int) (250)));
 //BA.debugLineNum = 107;BA.debugLine="lblCirculoPos2.Color = Colors.white";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 108;BA.debugLine="lblCirculoPos3.Color = Colors.white";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 109;BA.debugLine="lblCirculoPos1.Color = Colors.white";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 110;BA.debugLine="lblCirculoPos5.Color = Colors.white";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 }else if(_position>=1320) { 
 //BA.debugLineNum = 112;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255,150,200,2";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (150),(int) (200),(int) (250)));
 //BA.debugLineNum = 113;BA.debugLine="lblCirculoPos2.Color = Colors.white";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 114;BA.debugLine="lblCirculoPos3.Color = Colors.white";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 115;BA.debugLine="lblCirculoPos1.Color = Colors.white";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 116;BA.debugLine="lblCirculoPos4.Color = Colors.white";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 };
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
}
