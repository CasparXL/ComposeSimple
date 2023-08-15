package com.klsy.cashier.app.ui.view


import android.app.Presentation
import android.content.Context
import android.graphics.Outline
import android.view.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.R
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.util.*

/**
 * Opens a dialog with the given content.
 *
 * The dialog is visible as long as it is part of the composition hierarchy.
 * In order to let the user dismiss the Dialog, the implementation of [onDismissRequest] should
 * contain a way to remove to remove the dialog from the composition hierarchy.
 *
 * Example usage:
 *
 * @sample androidx.compose.ui.samples.DialogSample
 *
 * @param onDismissRequest Executes when the user tries to dismiss the dialog.
 * @param content The content to be displayed inside the dialog.
 */
@Composable
fun Presentation(
    display: Display,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val composition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)
    val dialogId = rememberSaveable { UUID.randomUUID() }
    val presentation = remember(view, density) {
        PresentationWrapper(
            onDismissRequest,
            view,
            layoutDirection,
            density,
            dialogId,
            display,
        ).apply {
            setContent(composition) {
                currentContent()
            }
        }
    }

    DisposableEffect(presentation) {
        presentation.show()

        onDispose {
            presentation.dismiss()
            presentation.disposeComposition()
        }
    }

    SideEffect {
        presentation.updateParameters(
            onDismissRequest = onDismissRequest,
            layoutDirection = layoutDirection
        )
    }
}

/**
 * Provides the underlying window of a dialog.
 *
 * Implemented by dialog's root layout.
 */
interface PresentationWindowProvider {
    val window: Window
}

@Suppress("ViewConstructor")
private class PresentationLayout(
    context: Context,
    override val window: Window
) : AbstractComposeView(context), PresentationWindowProvider {

    private var content: @Composable () -> Unit by mutableStateOf({})

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    init {
        layoutParams= LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
    }

    fun setContent(parent: CompositionContext, content: @Composable () -> Unit) {
        setParentCompositionContext(parent)
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
        createComposition()
    }

    @Composable
    override fun Content() {
        content()
    }
}

private class PresentationWrapper(
    private var onDismissRequest: () -> Unit,
    private val composeView: View,
    layoutDirection: LayoutDirection,
    density: Density,
    presentationId: UUID,
    display: Display,
) : Presentation(
    /**
     * [Window.setClipToOutline] is only available from 22+, but the style attribute exists on 21.
     * So use a wrapped context that sets this attribute for compatibility back to 21.
     */
    ContextThemeWrapper(composeView.context, R.style.DialogWindowTheme),
    display
),
    ViewRootForInspector {

    private val presentationLayout: PresentationLayout

    private val maxSupportedElevation = 30.dp

    override val subCompositionView: AbstractComposeView get() = presentationLayout

    init {
        val window = window ?: error("Presentation has no window")
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        presentationLayout = PresentationLayout(context, window).apply {
            // Set unique id for AbstractComposeView. This allows state restoration for the state
            // defined inside the Dialog via rememberSaveable()
            setTag(R.id.compose_view_saveable_id_tag, "Presentation:$presentationId")
            // Enable children to draw their shadow by not clipping them
            clipChildren = false
            // Allocate space for elevation
            with(density) { elevation = maxSupportedElevation.toPx() }
            // Simple outline to force window manager to allocate space for shadow.
            // Note that the outline affects clickable area for the dismiss listener. In case of
            // shapes like circle the area for dismiss might be to small (rectangular outline
            // consuming clicks outside of the circle).
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, result: Outline) {
                    result.setRect(0, 0, view.width, view.height)
                    // We set alpha to 0 to hide the view's shadow and let the composable to draw
                    // its own shadow. This still enables us to get the extra space needed in the
                    // surface.
                    result.alpha = 0f
                }
            }
        }

        /**
         * Disables clipping for [this] and all its descendant [ViewGroup]s until we reach a
         * [PresentationLayout] (the [ViewGroup] containing the Compose hierarchy).
         */
        fun ViewGroup.disableClipping() {
            clipChildren = false
            if (this is PresentationLayout) return
            for (i in 0 until childCount) {
                (getChildAt(i) as? ViewGroup)?.disableClipping()
            }
        }

        // Turn of all clipping so shadows can be drawn outside the window
        (window.decorView as? ViewGroup)?.disableClipping()
        setContentView(presentationLayout)
        presentationLayout.window.decorView.setViewTreeLifecycleOwner(composeView.findViewTreeLifecycleOwner())
        presentationLayout.window.decorView.setViewTreeViewModelStoreOwner(composeView.findViewTreeViewModelStoreOwner())
        presentationLayout.window.decorView.setViewTreeSavedStateRegistryOwner(composeView.findViewTreeSavedStateRegistryOwner())

        // Initial setup
        updateParameters(onDismissRequest, layoutDirection)
    }

    private fun setLayoutDirection(layoutDirection: LayoutDirection) {
        presentationLayout.layoutDirection = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
    }

    // TODO(b/159900354): Make the Android Dialog full screen and the scrim fully transparent

    fun setContent(parentComposition: CompositionContext, children: @Composable () -> Unit) {
        presentationLayout.setContent(parentComposition, children)
    }

    fun updateParameters(
        onDismissRequest: () -> Unit,
        layoutDirection: LayoutDirection
    ) {
        this.onDismissRequest = onDismissRequest
        setOnDismissListener {
            onDismissRequest()
        }
        setLayoutDirection(layoutDirection)
    }

    fun disposeComposition() {
        presentationLayout.disposeComposition()
    }

}