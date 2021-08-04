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

public class frmquehacer extends Activity implements B4AActivity{
	public static frmquehacer mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmquehacer");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmquehacer).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmquehacer");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmquehacer", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmquehacer) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmquehacer) Resume **");
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
		return frmquehacer.class;
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
            BA.LogInfo("** Activity (frmquehacer) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmquehacer) Pause event (activity is not paused). **");
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
            frmquehacer mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmquehacer) Resume **");
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
public static dhqi.social.share.socialshare _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirculopos6 = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scrollquehacer = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butexpand5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butexpand4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butexpand3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butexpand2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butexpand1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblque1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblque2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblque3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblque4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblque5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgquehacer1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtshare = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butshare = null;
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
public caza.mosquito.frmidentificarmosquito _vvvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frminfografias_main _frminfografias_main = null;
public caza.mosquito.frminstrucciones _vvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.frmlocalizacion _vvvvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.frmmapa _vvvvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.frmpoliticadatos _vvvvvvvvvvvvvvvvvvvvv1 = null;
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
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="Activity.LoadLayout(\"lay_QueHacerMain\")";
mostCurrent._activity.LoadLayout("lay_QueHacerMain",mostCurrent.activityBA);
 //BA.debugLineNum = 42;BA.debugLine="scrollQueHacer.Panel.LoadLayout(\"lay_queHacer_Pan";
mostCurrent._scrollquehacer.getPanel().LoadLayout("lay_queHacer_Paneles",mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="scrollQueHacer.Panel.Width = 2000dip";
mostCurrent._scrollquehacer.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2000)));
 //BA.debugLineNum = 44;BA.debugLine="imgQueHacer1.Left = Activity.Width /2 - imgQueHac";
mostCurrent._imgquehacer1.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-mostCurrent._imgquehacer1.getWidth()/(double)2));
 //BA.debugLineNum = 45;BA.debugLine="butExpand1.Left = Activity.Width /2 - butExpand1.";
mostCurrent._butexpand1.setLeft((int) (mostCurrent._activity.getWidth()/(double)2-mostCurrent._butexpand1.getWidth()/(double)2));
 //BA.debugLineNum = 46;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 53;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_click() throws Exception{
 //BA.debugLineNum = 165;BA.debugLine="Sub but_Cerrar_Click";
 //BA.debugLineNum = 166;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 167;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static String  _butexpand1_click() throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub butExpand1_Click";
 //BA.debugLineNum = 107;BA.debugLine="If butExpand1.Text = \"\" Then";
if ((mostCurrent._butexpand1.getText()).equals("")) { 
 //BA.debugLineNum = 108;BA.debugLine="butExpand1.Text = \"\"";
mostCurrent._butexpand1.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 109;BA.debugLine="lblQue1.visible= True";
mostCurrent._lblque1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 110;BA.debugLine="butExpand1.Top = butExpand1.Top + 40dip";
mostCurrent._butexpand1.setTop((int) (mostCurrent._butexpand1.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 }else {
 //BA.debugLineNum = 112;BA.debugLine="butExpand1.Text = \"\"";
mostCurrent._butexpand1.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 113;BA.debugLine="lblQue1.visible= False";
mostCurrent._lblque1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 114;BA.debugLine="butExpand1.Top = butExpand1.Top - 40dip";
mostCurrent._butexpand1.setTop((int) (mostCurrent._butexpand1.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 };
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return "";
}
public static String  _butexpand2_click() throws Exception{
 //BA.debugLineNum = 118;BA.debugLine="Sub butExpand2_Click";
 //BA.debugLineNum = 119;BA.debugLine="If butExpand2.Text = \"\" Then";
if ((mostCurrent._butexpand2.getText()).equals("")) { 
 //BA.debugLineNum = 120;BA.debugLine="butExpand2.Text = \"\"";
mostCurrent._butexpand2.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 121;BA.debugLine="lblQue2.visible= True";
mostCurrent._lblque2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="butExpand2.Top = butExpand2.Top + 40dip";
mostCurrent._butexpand2.setTop((int) (mostCurrent._butexpand2.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 }else {
 //BA.debugLineNum = 124;BA.debugLine="butExpand2.Text = \"\"";
mostCurrent._butexpand2.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 125;BA.debugLine="lblQue2.visible= False";
mostCurrent._lblque2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 126;BA.debugLine="butExpand2.Top = butExpand2.Top - 40dip";
mostCurrent._butexpand2.setTop((int) (mostCurrent._butexpand2.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 };
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _butexpand3_click() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub butExpand3_Click";
 //BA.debugLineNum = 131;BA.debugLine="If butExpand3.Text = \"\" Then";
if ((mostCurrent._butexpand3.getText()).equals("")) { 
 //BA.debugLineNum = 132;BA.debugLine="butExpand3.Text = \"\"";
mostCurrent._butexpand3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 133;BA.debugLine="lblQue3.visible= True";
mostCurrent._lblque3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 134;BA.debugLine="butExpand3.Top = butExpand3.Top + 40dip";
mostCurrent._butexpand3.setTop((int) (mostCurrent._butexpand3.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 }else {
 //BA.debugLineNum = 136;BA.debugLine="butExpand3.Text = \"\"";
mostCurrent._butexpand3.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 137;BA.debugLine="lblQue3.visible= False";
mostCurrent._lblque3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 138;BA.debugLine="butExpand3.Top = butExpand3.Top - 40dip";
mostCurrent._butexpand3.setTop((int) (mostCurrent._butexpand3.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))));
 };
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _butexpand4_click() throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub butExpand4_Click";
 //BA.debugLineNum = 143;BA.debugLine="If butExpand4.Text = \"\" Then";
if ((mostCurrent._butexpand4.getText()).equals("")) { 
 //BA.debugLineNum = 144;BA.debugLine="butExpand4.Text = \"\"";
mostCurrent._butexpand4.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 145;BA.debugLine="lblQue4.visible= True";
mostCurrent._lblque4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="butExpand4.Top = butExpand4.Top + 100dip";
mostCurrent._butexpand4.setTop((int) (mostCurrent._butexpand4.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100))));
 }else {
 //BA.debugLineNum = 148;BA.debugLine="butExpand4.Text = \"\"";
mostCurrent._butexpand4.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 149;BA.debugLine="lblQue4.visible= False";
mostCurrent._lblque4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 150;BA.debugLine="butExpand4.Top = butExpand4.Top - 100dip";
mostCurrent._butexpand4.setTop((int) (mostCurrent._butexpand4.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100))));
 };
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _butexpand5_click() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub butExpand5_Click";
 //BA.debugLineNum = 154;BA.debugLine="If butExpand5.Text = \"\" Then";
if ((mostCurrent._butexpand5.getText()).equals("")) { 
 //BA.debugLineNum = 155;BA.debugLine="butExpand5.Text = \"\"";
mostCurrent._butexpand5.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 156;BA.debugLine="lblQue5.visible= True";
mostCurrent._lblque5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 157;BA.debugLine="butExpand5.Top = butExpand5.Top + 120dip";
mostCurrent._butexpand5.setTop((int) (mostCurrent._butexpand5.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))));
 }else {
 //BA.debugLineNum = 159;BA.debugLine="butExpand5.Text = \"\"";
mostCurrent._butexpand5.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 160;BA.debugLine="lblQue5.visible= False";
mostCurrent._lblque5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="butExpand5.Top = butExpand5.Top - 120dip";
mostCurrent._butexpand5.setTop((int) (mostCurrent._butexpand5.getTop()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (120))));
 };
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
return "";
}
public static void  _butshare_click() throws Exception{
ResumableSub_butShare_Click rsub = new ResumableSub_butShare_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_butShare_Click extends BA.ResumableSub {
public ResumableSub_butShare_Click(caza.mosquito.frmquehacer parent) {
this.parent = parent;
}
caza.mosquito.frmquehacer parent;
int _iresult = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 174;BA.debugLine="Msgbox2Async(\"Compartirás lo que escribiste en Tw";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Compartirás lo que escribiste en Twitter y Facebook, ¿te parece bien?"),BA.ObjectToCharSequence("Compartir"),"Si","No","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 175;BA.debugLine="Wait For MsgBox_Result (iResult As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_iresult = (Integer) result[0];
;
 //BA.debugLineNum = 176;BA.debugLine="If iResult = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_iresult==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 177;BA.debugLine="Social.Initialize";
parent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0._initialize(processBA);
 //BA.debugLineNum = 178;BA.debugLine="Social.ShareMessage(\"¡Te cuento que hice hoy par";
parent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0._vv5("¡Te cuento que hice hoy para ayudar! "+parent.mostCurrent._txtshare.getText()+"Unite a Caza Mosquitos - @caza_mosquitos",parent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0._getvvv3());
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _iresult) throws Exception{
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private lblCirculoPos1 As Label";
mostCurrent._lblcirculopos1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private lblCirculoPos2 As Label";
mostCurrent._lblcirculopos2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private lblCirculoPos3 As Label";
mostCurrent._lblcirculopos3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private lblCirculoPos4 As Label";
mostCurrent._lblcirculopos4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private lblCirculoPos5 As Label";
mostCurrent._lblcirculopos5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private lblCirculoPos6 As Label";
mostCurrent._lblcirculopos6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private scrollQueHacer As HorizontalScrollView";
mostCurrent._scrollquehacer = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private butExpand5 As Button";
mostCurrent._butexpand5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private butExpand4 As Button";
mostCurrent._butexpand4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private butExpand3 As Button";
mostCurrent._butexpand3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private butExpand2 As Button";
mostCurrent._butexpand2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private butExpand1 As Button";
mostCurrent._butexpand1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private lblQue1 As Label";
mostCurrent._lblque1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lblQue2 As Label";
mostCurrent._lblque2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private lblQue3 As Label";
mostCurrent._lblque3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private lblQue4 As Label";
mostCurrent._lblque4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lblQue5 As Label";
mostCurrent._lblque5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private imgQueHacer1 As ImageView";
mostCurrent._imgquehacer1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private txtShare As EditText";
mostCurrent._txtshare = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private butShare As Button";
mostCurrent._butshare = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private Social As SocialShare";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new dhqi.social.share.socialshare();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _scrollquehacer_scrollchanged(int _position) throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub scrollQueHacer_ScrollChanged(Position As Int)";
 //BA.debugLineNum = 61;BA.debugLine="If Position < 550 Then";
if (_position<550) { 
 //BA.debugLineNum = 62;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 63;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 64;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 65;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 66;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 67;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 }else if(_position>=550 && _position<1500) { 
 //BA.debugLineNum = 69;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 70;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 71;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 72;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 73;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 74;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 }else if(_position>1500 && _position<2400) { 
 //BA.debugLineNum = 76;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 77;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 78;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 79;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 80;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 81;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 }else if(_position>2400 && _position<3500) { 
 //BA.debugLineNum = 83;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 84;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 85;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 86;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 87;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 88;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 }else if(_position>3500 && _position<4500) { 
 //BA.debugLineNum = 90;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 91;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 92;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 93;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 94;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 95;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 }else if(_position>=4300) { 
 //BA.debugLineNum = 97;BA.debugLine="lblCirculoPos6.Color = Colors.ARGB(255, 174, 228";
mostCurrent._lblcirculopos6.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (174),(int) (228),(int) (174)));
 //BA.debugLineNum = 98;BA.debugLine="lblCirculoPos1.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 99;BA.debugLine="lblCirculoPos2.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 100;BA.debugLine="lblCirculoPos3.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos3.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 101;BA.debugLine="lblCirculoPos4.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos4.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 //BA.debugLineNum = 102;BA.debugLine="lblCirculoPos5.Color = Colors.ARGB(255, 62, 119,";
mostCurrent._lblcirculopos5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (62),(int) (119),(int) (104)));
 };
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _share_finished() throws Exception{
 //BA.debugLineNum = 213;BA.debugLine="Sub Share_Finished";
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
}
