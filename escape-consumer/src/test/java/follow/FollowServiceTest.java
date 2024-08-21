package follow;

import com.sparta.domain.follow.dto.FollowStoreResponseDto;
import com.sparta.domain.follow.entity.Follow;
import com.sparta.domain.follow.repository.FollowRepository;
import com.sparta.domain.follow.service.FollowService;
import com.sparta.domain.store.entity.Store;
import com.sparta.domain.store.repository.StoreRepository;
import com.sparta.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private FollowService followService;

    private User user;
    private Store store;
    private Follow follow;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        store = mock(Store.class);
        follow = Follow.builder().user(user).store(store).build();
    }

    @Test
    public void 팔로우(){
        //given
        when(storeRepository.findByActiveStore(anyLong())).thenReturn(store);
        doNothing().when(followRepository).checkIfAlreadyFollowed(any(),any());
        when(followRepository.save(any())).thenReturn(follow);

        //when
        followService.followStore(1L, user);

        //then
        verify(storeRepository, times(1)).findByActiveStore(1L);
        verify(followRepository, times(1)).checkIfAlreadyFollowed(user, store);
        verify(followRepository, times(1)).save(any());
    }

    @Test
    public void 언팔로우(){
        //given
        when(storeRepository.findByActiveStore(any())).thenReturn(store);
        when(followRepository.getFollowOrThrow(any(), any())).thenReturn(follow);

        //when
        followService.unfollowStore(1L, user);

        //then
        verify(storeRepository, times(1)).findByActiveStore(1L);
        verify(followRepository, times(1)).getFollowOrThrow(user, store);
        verify(followRepository, times(1)).delete(follow);
    }

    @Test
    void 팔로우_카페_조회() {
        List<Follow> follows = new ArrayList<>();

        when(followRepository.findByGetStores(any())).thenReturn(follows);

        List<FollowStoreResponseDto> result = followService.getFollowStores(user);

        verify(followRepository, times(1)).findByGetStores(user);

        assertThat(result.size()).isEqualTo(follows.size());
    }

}
