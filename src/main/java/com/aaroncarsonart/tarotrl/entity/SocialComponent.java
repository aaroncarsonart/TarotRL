package com.aaroncarsonart.tarotrl.entity;

public interface SocialComponent {
    SocialComponent impl = new SocialComponentImpl();

    default boolean update() {
        return impl.update();
    }


    final class SocialComponentImpl implements SocialComponent {
        private int a = 1;

        public boolean update(){
            return true;
        }

    }
}
