package com.am.rxlearnings.async

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.am.rxlearnings.MainActivity
import com.am.rxlearnings.R
import com.bumptech.glide.Glide
import io.reactivex.Observable.create
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_async.*
import java.util.*

class AsyncFragment: Fragment() {

    // Create composite disposable so that you can destroy all the observables while
    // destroying the fragment
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_async, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCreateThubmbail.setOnClickListener {
            btnCreateThubmbail.isEnabled = false
            createThumbnail(etVideoURL.text.toString())
        }
    }

    @Throws(Throwable::class)
    private fun createThumbnail(videoPath: String){
        val disposable = create(ObservableOnSubscribe<Bitmap> {
            val bitmap: Bitmap?
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(
                    videoPath,
                    HashMap()
                )
                bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
            } catch (e: Exception) {
                it.onError(e)
                e.printStackTrace()
                throw Throwable(
                    "Exception in retrieve VideoFrameFromVideo(String videoPath)"
                            + e.message
                )
            } finally {
                mediaMetadataRetriever?.release()
            }
            it.onNext(bitmap!!)
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                btnCreateThubmbail.isEnabled = true
                if (ivThumbnail != null) {
                    Glide.with(activity as MainActivity)
                        .asBitmap()
                        .load(it)
                        .into(ivThumbnail)
                }
            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}