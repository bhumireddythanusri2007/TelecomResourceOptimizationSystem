package com.telecom.app;

import com.telecom.dao.NetworkCellDAO;
import com.telecom.model.NetworkCell;

import java.util.List;

public class NetworkCellTest {

    public static void main(String[] args) {

        NetworkCellDAO dao = new NetworkCellDAO();

        List<NetworkCell> cells = dao.getAllNetworkCells();

        System.out.println("Total network cells: " + cells.size());

        for (NetworkCell cell : cells) {
            System.out.println(cell);
        }
    }
}