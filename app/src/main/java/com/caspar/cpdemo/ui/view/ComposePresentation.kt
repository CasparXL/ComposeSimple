package com.caspar.cpdemo.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Presentation
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.contextaware.ContextAware
import androidx.activity.contextaware.ContextAwareHelper
import androidx.activity.contextaware.OnContextAvailableListener
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.util.concurrent.atomic.AtomicInteger

/**
 * @see androidx.activity.ComponentActivity
 */
open class ComposePresentation(
    outerContext: Context?,
    display: Display?,
    theme: Int = 0,
    @LayoutRes val contentLayoutId: Int = 0,
    windowType: Int = if (outerContext is Service || outerContext is Application)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        } else 0,
) :
    Presentation(ContextWrapper(outerContext), display, theme), ContextAware,
    LifecycleOwner,
    ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner,
    OnBackPressedDispatcherOwner {

    internal class NonConfigurationInstances() {
        var custom: Any? = null
        var viewModelStore: ViewModelStore? = null
    }

    private val ACTIVITY_RESULT_TAG = "android:support:activity-result"

    val mContextAwareHelper = ContextAwareHelper()
    private val mLifecycleRegistry = LifecycleRegistry(this)
    /* synthetic access */ val mSavedStateRegistryController =
        SavedStateRegistryController.create(this)

    // Lazily recreated from NonConfigurationInstances by getViewModelStore()
    private var mViewModelStore: ViewModelStore? = null
    private var mDefaultFactory: ViewModelProvider.Factory? = null

    private val mOnBackPressedDispatcher =
        OnBackPressedDispatcher { // Calling onBackPressed() on an Activity with its state saved can cause an
            // error on devices on API levels before 26. We catch that specific error and
            // throw all others.
            try {
                super.onBackPressed()
            } catch (e: IllegalStateException) {
                if (!TextUtils.equals(
                        e.message,
                        "Can not perform this action after onSaveInstanceState"
                    )
                ) {
                    throw e
                }
            }
        }

    private val mNextLocalRequestCode = AtomicInteger()

    /**
     * Default constructor for ComponentActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * [android.app.AppComponentFactory].
     */
    init {
        if (windowType != 0) {
            window?.setType(windowType)
        }
        val lifecycle: Lifecycle = lifecycle
            ?: throw IllegalStateException(
                ("getLifecycle() returned null in ComponentActivity's "
                        + "constructor. Please make sure you are lazily constructing your Lifecycle "
                        + "in the first call to getLifecycle() rather than relying on field "
                        + "initialization.")
            )
        if (Build.VERSION.SDK_INT >= 19) {
            lifecycle.addObserver(LifecycleEventObserver { source, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    val window = window
                    val decor = window?.peekDecorView()
                    if (decor != null) {
                        Api19Impl.cancelPendingInputEvents(decor)
                    }
                }
            })
        }
        lifecycle.addObserver(LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                // Clear out the available context
                mContextAwareHelper.clearAvailableContext()
                // And clear the ViewModelStore
//                if (!isChangingConfigurations()) {
                viewModelStore.clear()
//                }
            }
        })
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event
            ) {
                ensureViewModelStore()
                lifecycle.removeObserver(this)
            }
        })
        if (Build.VERSION.SDK_INT in 19..23) {
//            getLifecycle().addObserver(androidx.activity.ImmLeaksCleaner(this))
        }
        savedStateRegistry.registerSavedStateProvider(
            ACTIVITY_RESULT_TAG
        ) {
            val outState = Bundle()
//            mActivityResultRegistry.onSaveInstanceState(outState)
            outState
        }
        addOnContextAvailableListener { _ ->
//            val savedInstanceState: Bundle? = savedStateRegistry
//                .consumeRestoredStateForKey(ACTIVITY_RESULT_TAG)
//            if (savedInstanceState != null) {
//                mActivityResultRegistry.onRestoreInstanceState(savedInstanceState)
//            }
        }
    }


    /**
     * {@inheritDoc}
     *
     * If your ComponentActivity is annotated with [ContentView], this will
     * call [.setContentView] for you.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Restore the Saved State first so that it is available to
        // OnContextAvailableListener instances
        mSavedStateRegistryController.performRestore(savedInstanceState)
        mContextAwareHelper.dispatchOnContextAvailable(context)
        super.onCreate(savedInstanceState)
//        ReportFragment.injectIfNeededIn(this)
        if (contentLayoutId != 0) {
            setContentView(contentLayoutId)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onStart() {
        super.onStart()
        val lifecycle = lifecycle
        if (lifecycle is LifecycleRegistry) {
            lifecycle.currentState = Lifecycle.State.CREATED
            lifecycle.currentState = Lifecycle.State.STARTED
        }
    }

    override fun onStop() {
        super.onStop()
        val lifecycle = lifecycle
        if (lifecycle is LifecycleRegistry) {
            lifecycle.currentState = Lifecycle.State.DESTROYED
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onSaveInstanceState(): Bundle {
        val lifecycle = lifecycle
        if (lifecycle is LifecycleRegistry) {
            lifecycle.currentState = Lifecycle.State.CREATED
        }
        return super.onSaveInstanceState().apply {
            mSavedStateRegistryController.performSave(this)
        }
    }
//    @CallSuper
//    protected fun onSaveInstanceState(outState: Bundle) {
//        val lifecycle = lifecycle
//        if (lifecycle is LifecycleRegistry) {
//            lifecycle.currentState = Lifecycle.State.CREATED
//        }
//        super.onSaveInstanceState(outState)
//        mSavedStateRegistryController.performSave(outState)
//    }

//    /**
//     * Retain all appropriate non-config state.  You can NOT
//     * override this yourself!  Use a [androidx.lifecycle.ViewModel] if you want to
//     * retain your own non config state.
//     */
//    fun onRetainNonConfigurationInstance(): Any? {
//        // Maintain backward compatibility.
//        val custom = onRetainCustomNonConfigurationInstance()
//        var viewModelStore = mViewModelStore
//        if (viewModelStore == null) {
//            // No one called getViewModelStore(), so see if there was an existing
//            // ViewModelStore from our last NonConfigurationInstance
//            val nc = getLastNonConfigurationInstance() as NonConfigurationInstances?
//            if (nc != null) {
//                viewModelStore = nc.viewModelStore
//            }
//        }
//        if (viewModelStore == null && custom == null) {
//            return null
//        }
//        val nci = NonConfigurationInstances()
//        nci.custom = custom
//        nci.viewModelStore = viewModelStore
//        return nci
//    }

    /**
     * Use this instead of [.onRetainNonConfigurationInstance].
     * Retrieve later with [.getLastCustomNonConfigurationInstance].
     *
     */
    @Deprecated("Use a {@link androidx.lifecycle.ViewModel} to store non config state.")
    fun onRetainCustomNonConfigurationInstance(): Any? {
        return null
    }

    /**
     * Return the value previously returned from
     * [.onRetainCustomNonConfigurationInstance].
     *
     */
    @Deprecated(
        "Use a {@link androidx.lifecycle.ViewModel} to store non config state.",
        ReplaceWith("null")
    )
    fun getLastCustomNonConfigurationInstance(): Any? {
        val nc = getLastNonConfigurationInstance() as NonConfigurationInstances?
        return nc?.custom
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        initViewTreeOwners()
        super.setContentView(layoutResID)
    }

    override fun setContentView(@SuppressLint("UnknownNullness", "MissingNullability") view: View) {
        initViewTreeOwners()
        super.setContentView(view)
    }

//    fun setContentView(@SuppressLint("UnknownNullness", "MissingNullability") view: View?) {
//        initViewTreeOwners()
//        super.setContentView(view!!)
//    }

    override fun setContentView(
        @SuppressLint("UnknownNullness", "MissingNullability") view: View,
        @SuppressLint("UnknownNullness", "MissingNullability") params: ViewGroup.LayoutParams?
    ) {
        initViewTreeOwners()
        super.setContentView(view, params)
    }

    override fun addContentView(
        @SuppressLint("UnknownNullness", "MissingNullability") view: View,
        @SuppressLint("UnknownNullness", "MissingNullability") params: ViewGroup.LayoutParams?
    ) {
        initViewTreeOwners()
        super.addContentView(view, params)
    }
//    fun addContentView(
//        @SuppressLint("UnknownNullness", "MissingNullability") view: View?,
//        @SuppressLint("UnknownNullness", "MissingNullability") params: ViewGroup.LayoutParams?
//    ) {
//        initViewTreeOwners()
//        super.addContentView(view!!, params)
//    }

    private fun initViewTreeOwners() {
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        window?.decorView?.also {
            it.setViewTreeLifecycleOwner(this)
            it.setViewTreeViewModelStoreOwner(this)
            it.setViewTreeSavedStateRegistryOwner(this)
        }
    }

    override fun peekAvailableContext(): Context? {
        return mContextAwareHelper.peekAvailableContext()
    }

    /**
     * {@inheritDoc}
     *
     * Any listener added here will receive a callback as part of
     * `super.onCreate()`, but importantly **before** any other
     * logic is done (including calling through to the framework
     * [Activity.onCreate] with the exception of restoring the state
     * of the [SavedStateRegistry][.getSavedStateRegistry] for use in your listener.
     */
    override fun addOnContextAvailableListener(
        listener: OnContextAvailableListener
    ) {
        mContextAwareHelper.addOnContextAvailableListener(listener)
    }

    override fun removeOnContextAvailableListener(
        listener: OnContextAvailableListener
    ) {
        mContextAwareHelper.removeOnContextAvailableListener(listener)
    }

    @Suppress("MemberVisibilityCanBePrivate", "HasPlatformType")
    fun getApplication() = context.applicationContext?.let {
        if (it is Application) it else null
    }


    private fun getLastNonConfigurationInstance() = if (context is ContextWrapper) {
        (context as ContextWrapper).baseContext
    } else {
        context
    }.let {
        if (it is Activity) it.lastNonConfigurationInstance else null
    }

    fun  /* synthetic access */ensureViewModelStore() {
        if (mViewModelStore == null) {
            val nc = getLastNonConfigurationInstance() as NonConfigurationInstances?
            if (nc != null) {
                // Restore the ViewModelStore from NonConfigurationInstances
                mViewModelStore = nc.viewModelStore
            }
            if (mViewModelStore == null) {
                mViewModelStore = ViewModelStore()
            }
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The [OnBackPressedDispatcher][.getOnBackPressedDispatcher] will be given a
     * chance to handle the back button before the default behavior of
     * [android.app.Activity.onBackPressed] is invoked.
     *
     * @see .getOnBackPressedDispatcher
     */
    @MainThread
    override fun onBackPressed() {
        mOnBackPressedDispatcher.onBackPressed()
    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" + "      passing in a {@link StartActivityForResult} object for the {@link ActivityResultContract}.")
//    fun startActivityForResult(
//        @SuppressLint("UnknownNullness") intent: Intent?,
//        requestCode: Int
//    ) {
//        super.startActivityForResult(intent, requestCode)
//    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" + "      passing in a {@link StartActivityForResult} object for the {@link ActivityResultContract}.")
//    fun startActivityForResult(
//        @SuppressLint("UnknownNullness") intent: Intent?,
//        requestCode: Int, options: Bundle?
//    ) {
//        super.startActivityForResult(intent, requestCode, options)
//    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" + "      passing in a {@link StartIntentSenderForResult} object for the\n" + "      {@link ActivityResultContract}.")
//    @Throws(
//        SendIntentException::class
//    )
//    fun startIntentSenderForResult(
//        @SuppressLint("UnknownNullness") intent: IntentSender?,
//        requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int,
//        extraFlags: Int
//    ) {
//        super.startIntentSenderForResult(
//            intent, requestCode, fillInIntent, flagsMask, flagsValues,
//            extraFlags
//        )
//    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" + "      passing in a {@link StartIntentSenderForResult} object for the\n" + "      {@link ActivityResultContract}.")
//    @Throws(
//        SendIntentException::class
//    )
//    fun startIntentSenderForResult(
//        @SuppressLint("UnknownNullness") intent: IntentSender?,
//        requestCode: Int, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int,
//        extraFlags: Int, options: Bundle?
//    ) {
//        super.startIntentSenderForResult(
//            intent, requestCode, fillInIntent, flagsMask, flagsValues,
//            extraFlags, options
//        )
//    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @CallSuper
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n" + "      with the appropriate {@link ActivityResultContract} and handling the result in the\n" + "      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (!mActivityResultRegistry.dispatchResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }

//    /**
//     * {@inheritDoc}
//     *
//     */
//    @CallSuper
//    @Deprecated("use\n" + "      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n" + "      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n" + "      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}.")
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        if (!mActivityResultRegistry.dispatchResult(
//                requestCode, Activity.RESULT_OK, Intent()
//                    .putExtra(RequestMultiplePermissions.EXTRA_PERMISSIONS, permissions)
//                    .putExtra(
//                        RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS,
//                        grantResults
//                    )
//            )
//        ) {
//            if (VERSION.SDK_INT >= 23) {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//            }
//        }
//    }

    @Deprecated(
        "not support in presentation",
        ReplaceWith("nothing")
    )
//    override fun <I, O> registerForActivityResult(
//        contract: ActivityResultContract<I, O>,
//        registry: ActivityResultRegistry,
//        callback: ActivityResultCallback<O>
//    ): ActivityResultLauncher<I> {
//        return registry.register(
//            "activity_rq#" + mNextLocalRequestCode.getAndIncrement(), this, contract, callback
//        )
//    }


//    fun reportFullyDrawn() {
//        try {
//            if (Trace.isEnabled()) {
//                // TODO: Ideally we'd include getComponentName() (as later versions of platform
//                //  do), but b/175345114 needs to be addressed.
//                Trace.beginSection("reportFullyDrawn() for ComponentActivity")
//            }
//            if (VERSION.SDK_INT > 19) {
//                super.reportFullyDrawn()
//            } else if (VERSION.SDK_INT == 19 && ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.UPDATE_DEVICE_STATS
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                // On API 19, the Activity.reportFullyDrawn() method requires the
//                // UPDATE_DEVICE_STATS permission, otherwise it throws an exception. Instead of
//                // throwing, we fall back to a no-op call.
//                super.reportFullyDrawn()
//            }
//            // The Activity.reportFullyDrawn() got added in API 19, fall back to a no-op call if
//            // this method gets called on devices with an earlier version.
//        } finally {
//            Trace.endSection()
//        }
//    }

    @RequiresApi(19)
    internal object Api19Impl {
        fun cancelPendingInputEvents(view: View) {
            view.cancelPendingInputEvents()
        }
    }

    override val lifecycle: Lifecycle
        get() = mLifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = mSavedStateRegistryController.savedStateRegistry
    override val onBackPressedDispatcher: OnBackPressedDispatcher
        get() = mOnBackPressedDispatcher
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() {
            if (getApplication() == null) {
                throw IllegalStateException(
                    ("Your activity is not yet attached to the "
                            + "Application instance. You can't request ViewModel before onCreate call.")
                )
            }
            if (mDefaultFactory == null) {
                mDefaultFactory = SavedStateViewModelFactory(
                    getApplication(),
                    this,
                    /*if (getIntent() != null) getIntent().getExtras() else*/ null
                )
            }
            return mDefaultFactory!!
        }
    override val viewModelStore: ViewModelStore
        get() {
            if (getApplication() == null) {
                throw IllegalStateException(
                    "Your activity is not yet attached to the "
                            + "Application instance. You can't request ViewModel before onCreate call."
                )
            }
            ensureViewModelStore()
            return mViewModelStore!!
        }
}





/**
 * Composes the given composable into the given activity. The [content] will become the root view
 * of the given activity.
 *
 * This is roughly equivalent to calling [ComponentActivity.setContentView] with a [ComposeView]
 * i.e.:
 *
 * ```
 * setContentView(
 *   ComposeView(this).apply {
 *     setContent {
 *       MyComposableContent()
 *     }
 *   }
 * )
 * ```
 *
 * @param parent The parent composition reference to coordinate scheduling of composition updates
 * @param content A `@Composable` function declaring the UI contents
 */
public fun ComposePresentation.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit
) {
    val existingComposeView = window?.decorView
        ?.findViewById<ViewGroup>(android.R.id.content)
        ?.getChildAt(0) as? ComposeView

    if (existingComposeView != null) with(existingComposeView) {
        setParentCompositionContext(parent)
        setContent(content)
    } else ComposeView(context).apply {
        // Set content and parent **before** setContentView
        // to have ComposeView create the composition on attach
        setParentCompositionContext(parent)
        setContent(content)
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        setOwners()
        setContentView(this, DefaultActivityContentLayoutParams)
    }
}

private val DefaultActivityContentLayoutParams = ViewGroup.LayoutParams(
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
)

/**
 * These owners are not set before AppCompat 1.3+ due to a bug, so we need to set them manually in
 * case developers are using an older version of AppCompat.
 */
private fun ComposePresentation.setOwners() {
    window?.decorView?.also {
        if (it.findViewTreeLifecycleOwner() == null){
            it.setViewTreeLifecycleOwner(this)
        }
        if (it.findViewTreeViewModelStoreOwner() == null){
            it.setViewTreeViewModelStoreOwner(this)
        }
        if (it.findViewTreeSavedStateRegistryOwner() == null) {
            it.setViewTreeSavedStateRegistryOwner(this)
        }
    }
}