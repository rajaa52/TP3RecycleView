package ma.rajaa.tp3listcelebrities.service;

import java.util.ArrayList;
import java.util.List;

import ma.rajaa.tp3listcelebrities.classes.Star;
import ma.rajaa.tp3listcelebrities.dao.IDao;


import java.util.ArrayList;
import java.util.List;

public class StarService implements IDao<Star> {
    private List<Star> stars;
    private static StarService instance;

    private StarService() {
        stars = new ArrayList<>();
    }

    public static StarService getInstance() {
        if (instance == null) {
            instance = new StarService();
        }
        return instance;
    }

    @Override
    public boolean create(Star star) {
        return stars.add(star);
    }

    @Override
    public boolean update(Star star) {
        for (Star s : stars) {
            if (s.getId() == star.getId()) {
                s.setImg(star.getImg());
                s.setName(star.getName());
                s.setStar(star.getStar());
            }
        }
        return true;
    }

    @Override
    public boolean delete(Star star) {
        return stars.remove(star);
    }

    @Override
    public Star findById(int id) {
        for (Star s : stars) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<Star> findAll() {
        return stars;
    }
}
