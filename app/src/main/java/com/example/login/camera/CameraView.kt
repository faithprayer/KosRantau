package com.example.login.camera

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraView (context: Context?, private var mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private var cameraID = -1
    private val mHolder: SurfaceHolder

    init {
        mCamera.setDisplayOrientation(90)
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("error", "Camera eror on SurfaceCreated" + e.message)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        if (mHolder.surface == null) return
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        }catch (e: IOException) {
            Log.d("Error", "Camera error on SurfaceChanged" + e.message)
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mCamera.stopPreview()
        mCamera.release()
    }

    fun cameraSwitch(){
        if (mCamera != null) {
            mCamera.stopPreview()
        }
        mCamera.release()
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                cameraID = i
        }
        if(cameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT
        }
        else if (cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT){
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        else{
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT
        }

        mCamera = Camera.open(cameraID)

        mCamera.setDisplayOrientation(90)
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("Error", "Camera error on switchCamera" + e.message)
        }
        mCamera.startPreview()
    }
}