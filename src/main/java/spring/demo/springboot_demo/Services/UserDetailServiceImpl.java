package spring.demo.springboot_demo.Services;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.demo.springboot_demo.Cache.Cache;
import spring.demo.springboot_demo.Entities.User;
import spring.demo.springboot_demo.Repositories.UserRepository;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private Cache cache;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) cache.getItem("user/" + username, User.class);

        if (user == null) {
            user = userRepository.findByEmail(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException("No user found. Username tried: " + username);
        }
        cache.setItem("user/" + username, user);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
