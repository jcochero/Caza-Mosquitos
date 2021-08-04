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

public class frmcomofotos extends Activity implements B4AActivity{
	public static frmcomofotos mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmcomofotos");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmcomofotos).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmcomofotos");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmcomofotos", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmcomofotos) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmcomofotos) Resume **");
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
		return frmcomofotos.class;
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
            BA.LogInfo("** Activity (frmcomofotos) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmcomofotos) Pause event (activity is not paused). **");
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
            frmcomofotos mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmcomofotos) Resume **");
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
public anywheresoftware.b4a.objects.LabelWrapper _lbltitulo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltexto1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgtexto1 = null;
public de.amberhome.viewpager.AHPageContainer _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public de.amberhome.viewpager.AHViewPager _ahviewpager1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblslide = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.starter _vvvvvvvvvvvvvvvvvv4 = null;
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
anywheresoftware.b4a.objects.PanelWrapper[] _pan = null;
 //BA.debugLineNum = 22;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 25;BA.debugLine="Dim Pan(4) As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper[(int) (4)];
{
int d0 = _pan.length;
for (int i0 = 0;i0 < d0;i0++) {
_pan[i0] = new anywheresoftware.b4a.objects.PanelWrapper();
}
}
;
 //BA.debugLineNum = 27;BA.debugLine="Pan(0).Initialize(\"\")";
_pan[(int) (0)].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 28;BA.debugLine="Pan(1).Initialize(\"\")";
_pan[(int) (1)].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 29;BA.debugLine="Pan(2).Initialize(\"\")";
_pan[(int) (2)].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 30;BA.debugLine="Pan(3).Initialize(\"\")";
_pan[(int) (3)].Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 33;BA.debugLine="Container.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 36;BA.debugLine="Pan(0).LoadLayout(\"layComoFotos_Panels\")";
_pan[(int) (0)].LoadLayout("layComoFotos_Panels",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 38;BA.debugLine="lblTitulo.Text  = \"Cómo capturar mosquitos\"";
mostCurrent._lbltitulo.setText(BA.ObjectToCharSequence("Cómo capturar mosquitos"));
 //BA.debugLineNum = 39;BA.debugLine="lblTexto1.Text  = \"Si quieres sorprendernos con";
mostCurrent._lbltexto1.setText(BA.ObjectToCharSequence("Si quieres sorprendernos con la calidad de tus fotos, atrapa el mosquito poniendo un vaso encima, y usa una hoja de papel a modo de tapa."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 };
 //BA.debugLineNum = 47;BA.debugLine="lblTitulo.Width = 100%x";
mostCurrent._lbltitulo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 48;BA.debugLine="lblTexto1.Width = 90%x";
mostCurrent._lbltexto1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 49;BA.debugLine="imgTexto1.RemoveView";
mostCurrent._imgtexto1.RemoveView();
 //BA.debugLineNum = 50;BA.debugLine="imgTexto1.Initialize(\"\")";
mostCurrent._imgtexto1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 51;BA.debugLine="imgTexto1.Bitmap = LoadBitmapSample(File.DirAsset";
mostCurrent._imgtexto1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"comofotos1.jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 //BA.debugLineNum = 52;BA.debugLine="imgTexto1.Gravity = Gravity.FILL";
mostCurrent._imgtexto1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 53;BA.debugLine="Pan(0).AddView(imgTexto1,(Activity.Width/2 - 110d";
_pan[(int) (0)].AddView((android.view.View)(mostCurrent._imgtexto1.getObject()),(int) ((mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (110)))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (220)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 54;BA.debugLine="Container.AddPageAt(Pan(0), \"panel0\", 0)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.AddPageAt((android.view.View)(_pan[(int) (0)].getObject()),"panel0",(int) (0));
 //BA.debugLineNum = 56;BA.debugLine="Pan(1).LoadLayout(\"layComoFotos_Panels\")";
_pan[(int) (1)].LoadLayout("layComoFotos_Panels",mostCurrent.activityBA);
 //BA.debugLineNum = 57;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 58;BA.debugLine="lblTitulo.Text  = \"Cómo capturar insectos\"";
mostCurrent._lbltitulo.setText(BA.ObjectToCharSequence("Cómo capturar insectos"));
 //BA.debugLineNum = 59;BA.debugLine="lblTexto1.Text  = \"Coloca el vaso en una bolsa (";
mostCurrent._lbltexto1.setText(BA.ObjectToCharSequence("Coloca el vaso en una bolsa (ver fotos), hazle un nudo y guárdalo una hora en el freezer."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 };
 //BA.debugLineNum = 68;BA.debugLine="lblTitulo.Width = 100%x";
mostCurrent._lbltitulo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 69;BA.debugLine="lblTexto1.Width = 90%x";
mostCurrent._lbltexto1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 70;BA.debugLine="imgTexto1.RemoveView";
mostCurrent._imgtexto1.RemoveView();
 //BA.debugLineNum = 71;BA.debugLine="imgTexto1.Initialize(\"\")";
mostCurrent._imgtexto1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 72;BA.debugLine="imgTexto1.Bitmap = LoadBitmapSample(File.DirAsset";
mostCurrent._imgtexto1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"comofotos11.jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 //BA.debugLineNum = 73;BA.debugLine="imgTexto1.Gravity = Gravity.FILL";
mostCurrent._imgtexto1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 74;BA.debugLine="Pan(1).AddView(imgTexto1,Activity.Width/2 - 35%x,";
_pan[(int) (1)].AddView((android.view.View)(mostCurrent._imgtexto1.getObject()),(int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (220)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 75;BA.debugLine="Container.AddPageAt(Pan(1), \"panel1\", 1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.AddPageAt((android.view.View)(_pan[(int) (1)].getObject()),"panel1",(int) (1));
 //BA.debugLineNum = 77;BA.debugLine="Pan(2).LoadLayout(\"layComoFotos_Panels\")";
_pan[(int) (2)].LoadLayout("layComoFotos_Panels",mostCurrent.activityBA);
 //BA.debugLineNum = 78;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 79;BA.debugLine="lblTitulo.Text  = \"Cómo preparar el insecto\"";
mostCurrent._lbltitulo.setText(BA.ObjectToCharSequence("Cómo preparar el insecto"));
 //BA.debugLineNum = 80;BA.debugLine="lblTexto1.Text  = \"Luego podrás sacar el mosquit";
mostCurrent._lbltexto1.setText(BA.ObjectToCharSequence("Luego podrás sacar el mosquito del recipiente y apoyarlo sobre una superficie clara para realizar buenas fotos."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 };
 //BA.debugLineNum = 89;BA.debugLine="lblTitulo.Width = 100%x";
mostCurrent._lbltitulo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 90;BA.debugLine="lblTexto1.Width = 90%x";
mostCurrent._lbltexto1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 91;BA.debugLine="imgTexto1.RemoveView";
mostCurrent._imgtexto1.RemoveView();
 //BA.debugLineNum = 92;BA.debugLine="imgTexto1.Initialize(\"\")";
mostCurrent._imgtexto1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 93;BA.debugLine="imgTexto1.Bitmap = LoadBitmapSample(File.DirAsset";
mostCurrent._imgtexto1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"comofotos14.jpg",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 //BA.debugLineNum = 94;BA.debugLine="imgTexto1.Gravity = Gravity.FILL";
mostCurrent._imgtexto1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 95;BA.debugLine="Pan(2).AddView(imgTexto1,Activity.Width/2 - 35%x,";
_pan[(int) (2)].AddView((android.view.View)(mostCurrent._imgtexto1.getObject()),(int) (mostCurrent._activity.getWidth()/(double)2-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (220)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 96;BA.debugLine="Container.AddPageAt(Pan(2), \"panel2\", 2)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.AddPageAt((android.view.View)(_pan[(int) (2)].getObject()),"panel2",(int) (2));
 //BA.debugLineNum = 98;BA.debugLine="Pan(3).LoadLayout(\"layComoFotos_Panels\")";
_pan[(int) (3)].LoadLayout("layComoFotos_Panels",mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 100;BA.debugLine="lblTitulo.Text  = \"¡IMPORTANTE!\"";
mostCurrent._lbltitulo.setText(BA.ObjectToCharSequence("¡IMPORTANTE!"));
 //BA.debugLineNum = 101;BA.debugLine="lblTexto1.Text  = \"Las fotos y la carga del dato";
mostCurrent._lbltexto1.setText(BA.ObjectToCharSequence("Las fotos y la carga del dato tiene que ser en las proximidades de donde se encontró el insecto  para obtener información exacta del lugar."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 104;BA.debugLine="lblTitulo.Text  = \"¡IMPORTANT!\"";
mostCurrent._lbltitulo.setText(BA.ObjectToCharSequence("¡IMPORTANT!"));
 //BA.debugLineNum = 105;BA.debugLine="lblTexto1.Text  = \"If you capture the insect, re";
mostCurrent._lbltexto1.setText(BA.ObjectToCharSequence("If you capture the insect, remember to mark the correct location where it was collected when you send the report through the app."));
 };
 //BA.debugLineNum = 110;BA.debugLine="lblTexto1.Top = 30%y";
mostCurrent._lbltexto1.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 111;BA.debugLine="lblTitulo.Width = 100%x";
mostCurrent._lbltitulo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 112;BA.debugLine="lblTexto1.Width = 90%x";
mostCurrent._lbltexto1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 113;BA.debugLine="imgTexto1.Bitmap = Null";
mostCurrent._imgtexto1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 114;BA.debugLine="Container.AddPageAt(Pan(3), \"panel3\", 3)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.AddPageAt((android.view.View)(_pan[(int) (3)].getObject()),"panel3",(int) (3));
 //BA.debugLineNum = 116;BA.debugLine="Activity.LoadLayout(\"layComoFotos\")";
mostCurrent._activity.LoadLayout("layComoFotos",mostCurrent.activityBA);
 //BA.debugLineNum = 117;BA.debugLine="AHViewPager1.RemoveView";
mostCurrent._ahviewpager1.RemoveView();
 //BA.debugLineNum = 118;BA.debugLine="AHViewPager1.Initialize2(Container,\"Container\")";
mostCurrent._ahviewpager1.Initialize2(mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2,"Container");
 //BA.debugLineNum = 119;BA.debugLine="Activity.AddView(AHViewPager1, 0dip, 0dip, Activi";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._ahviewpager1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),mostCurrent._activity.getWidth(),(int) (mostCurrent._activity.getHeight()-mostCurrent._lblslide.getHeight()));
 //BA.debugLineNum = 120;BA.debugLine="lblSlide.Top = Activity.Height - lblSlide.Height";
mostCurrent._lblslide.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._lblslide.getHeight()));
 //BA.debugLineNum = 121;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 122;BA.debugLine="TranslateGUI";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 141;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _btnvolverfotos_click() throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub btnVolverFotos_Click";
 //BA.debugLineNum = 147;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 148;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Private lblTitulo As Label";
mostCurrent._lbltitulo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private lblTexto1 As Label";
mostCurrent._lbltexto1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private imgTexto1 As ImageView";
mostCurrent._imgtexto1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim Container As AHPageContainer";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new de.amberhome.viewpager.AHPageContainer();
 //BA.debugLineNum = 18;BA.debugLine="Dim AHViewPager1 As AHViewPager";
mostCurrent._ahviewpager1 = new de.amberhome.viewpager.AHViewPager();
 //BA.debugLineNum = 19;BA.debugLine="Private lblSlide As Label";
mostCurrent._lblslide = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub TranslateGUI";
 //BA.debugLineNum = 127;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 128;BA.debugLine="lblSlide.Text = \"   desliza para más informació";
mostCurrent._lblslide.setText(BA.ObjectToCharSequence("   desliza para más información   "));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 130;BA.debugLine="lblSlide.Text = \"   slide for more information";
mostCurrent._lblslide.setText(BA.ObjectToCharSequence("   slide for more information   "));
 };
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
}
