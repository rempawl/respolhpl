package com.example.respolhpl.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.example.respolhpl.R
import com.example.respolhpl.databinding.FragmentHomeBinding
import com.example.respolhpl.paging.LoadMoreManager
import com.example.respolhpl.paging.PagedLazyColumn
import com.example.respolhpl.paging.pagedContent
import com.example.respolhpl.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


// todo category filters
// todo compose theme
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var binding: FragmentHomeBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val listState = rememberLazyListState()
                    val pagingData by viewModel.pagingData.collectAsState()
                    LoadMoreManager(
                        listState = listState,
                        onLoadMore = { viewModel.loadMore() }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                            .padding(top = 56.dp) // todo move toolbar from xml to compose
                    ) {
                        PagedLazyColumn(
                            listState = listState,
                        ) {
                            pagedContent(
                                data = pagingData,
                                retry = { viewModel.retry() },
                                emptyPlaceholder = { Text(text = "empty data") }, //todo empty placeholder
                                itemView = { ProductItem(it) }
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }

    @Composable
    private fun ProductItem(item: HomeViewModel.ProductMinimalListItem) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .clickable { viewModel.navigateToProductDetails(item.product.id) }
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .clip(
                        MaterialTheme.shapes.medium.copy(
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    ),
                painter = rememberAsyncImagePainter(
                    model = item.product.thumbnailSrc,
                    contentScale = ContentScale.FillBounds
                ), contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(text = item.product.name)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(
                        R.string.price,
                        item.product.price
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupBinding()
        setupObservers()
    }

    private fun setupObservers() = with(viewLifecycleOwner.lifecycleScope) {
        viewModel.shouldNavigate
            .map { it.id }
            .onEach { id -> navigateToProductDetails(id) }
            .launchIn(this)
    }

    private fun FragmentHomeBinding.setupBinding() {
        toolbar.apply {
            label.text = getString(R.string.label_main)
            cartBtn.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionNavHomeToCartFragment()) }
        }
    }

    private fun navigateToProductDetails(id: Int) {
        findNavController().navigate(
            HomeFragmentDirections.navigationHomeToProductDetails(id)
        )
    }
}
