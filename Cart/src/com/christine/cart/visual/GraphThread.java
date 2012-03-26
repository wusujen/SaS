package com.christine.cart.visual;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GraphThread extends Thread {
	private SurfaceHolder _surfaceHolder;
	private GraphView _graph;
	private boolean _run = false;
	
	public GraphThread(SurfaceHolder surfaceHolder, GraphView graph){
		_surfaceHolder = surfaceHolder;
		_graph = graph;
	}
	
	public void setRunning (boolean run){
		_run = run;
	}
	
	@Override
	public void run(){
		Canvas c;
		
		while(_run){
			c = null;
			try{
				c = _surfaceHolder.lockCanvas(null);
				c.drawARGB(255,255,255,255);
				synchronized(_surfaceHolder){
					_graph.onDraw(c);
				}
			} finally{
				if(c!=null){
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
