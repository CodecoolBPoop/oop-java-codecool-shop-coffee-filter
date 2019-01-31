package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoSQL extends DataBaseConnect implements ProductDao {
    private static ProductDaoSQL instance = null;

    public static ProductDaoSQL getInstance() {
        if (instance == null) {
            instance = new ProductDaoSQL();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        String name = product.getName();
        String description = product.getDescription();
        float price = product.getFloatPrice();
        int stock = 1;
        String currencyString = product.getCurrency();
        String supplierString = product.getSupplier().getSupplierName();
        String productCategoryString = product.getProductCategory().getName();

        String sql =
                "WITH curid as (SELECT id FROM currencies WHERE currencies.currency = '" + currencyString + "' RETURNING id) "
                        + "WITH supid as (SELECT id FROM suppliers WHERE suppliers.name = '" + supplierString + "' RETURNING id) "
                        + "WITH prid as (SELECT id FROM product_category WHERE product_category.name = '" + productCategoryString + "' RETURNING id) "
                        + "INSERT INTO products (name, description, price, stock, currency, supplier_id, product_category_id)"
                        + "VALUES (?, ?, ?, ?, (SELECT id FROM curid), (SELECT id FROM supid), (SELECT id FROM prid))";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setString(1, name);
            pstatement.setString(2, description);
            pstatement.setFloat(3, price);
            pstatement.setInt(4, stock);

            pstatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product find(int id) {
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE products.id=?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println(id);
                    return getProductFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        String sql = "UPDATE TABLE products SET active = false WHERE id = ?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);
            pstatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE products.active = TRUE";

        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        int supId = supplier.getId();
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE s.id = ? AND products.active = TRUE";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, supId);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        int pcID = productCategory.getId();
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE pc.id = ? AND products.active = TRUE";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, pcID);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getBy(int supplierID, int productCategoryID) {
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE s.id = ? AND pc.id = ? AND products.active = TRUE";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, supplierID);
            pstatement.setInt(2, productCategoryID);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getBySupplier(int supplierID) {
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE s.id = ? AND products.active = TRUE";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, supplierID);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getByProductCategory(int productCategoryID) {
        List<Product> data = new ArrayList<>();
        String sql = "SELECT products.id, products.name, products.description, products.price, products.stock, products.active, " +
                "s.id as supplier_id, s.name as supplier_name, s.description as supplier_desc, " +
                "pc.id as pc_id, pc.name as pc_name, pc.description as pc_desc, pc.department as pc_dept, " +
                "c.currency FROM products " +
                "LEFT JOIN suppliers s ON products.supplier_id = s.id " +
                "LEFT JOIN product_category pc ON products.product_category_id = pc.id " +
                "LEFT JOIN currencies c ON products.currency = c.id " +
                "WHERE pc.id = ? AND products.active = TRUE";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, productCategoryID);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    data.add(getProductFromResultSet(resultSet));
                }
                return data;
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Product getProductFromResultSet(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        float price = resultSet.getFloat("price");
        int stock = resultSet.getInt("stock");
        boolean active = resultSet.getBoolean("active");
        int supplierId = resultSet.getInt("supplier_id");
        String supplierName = resultSet.getString("supplier_name");
        String supplierDesc = resultSet.getString("supplier_desc");
        int productCategoryId = resultSet.getInt("pc_id");
        String productCategoryName = resultSet.getString("pc_name");
        String productCategoryDesc = resultSet.getString("pc_desc");
        String productCategoryDept = resultSet.getString("pc_dept");
        String currencyName = resultSet.getString("currency");

        Supplier givenSupp = new Supplier(supplierId, supplierName, supplierDesc);
        ProductCategory givenPC = new ProductCategory(productCategoryId, productCategoryName, productCategoryDesc, productCategoryDept);

        return new Product(id, name, price, currencyName, description, givenPC, givenSupp);
    }
}
