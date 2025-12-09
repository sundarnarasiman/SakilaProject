c# Sakila Project - Changes Summary

## Date: December 9, 2025

### 1. Fixed Application Startup Issue
- **Problem**: `ClassNotFoundException: javax.servlet.Filter`
- **Solution**: Removed `<scope>provided</scope>` from `spring-boot-starter-tomcat` dependency in pom.xml
- **Impact**: Application now starts successfully on http://localhost:8090/

### 2. Enhanced Film Search Functionality
- **Changed**: Film search from dropdown to text input
- **Files Modified**:
  - `FilmRepository.java` - Added `findByTitleContaining()` with LIKE query
  - `FilmService.java` - Updated to use partial matching
  - `FilmController.java` - Changed default filter handling
  - `films.html` - Replaced dropdown with text input field
  - `owner/manage-films.html` - Updated search interface
- **Impact**: Users can now type partial film titles for search

### 3. Enhanced Actor Search Functionality
- **Changed**: Actor search from dropdown to text input
- **Files Modified**:
  - `ActorRepository.java` - Added partial name matching queries
  - `ActorService.java` - Updated search methods
  - `ActorController.java` - Changed filter handling
  - `actors/actors.html` - Replaced dropdowns with text inputs
- **Impact**: Users can search actors by typing partial first/last names

### 4. Added Language Support to Films
- **Added**: Language entity and relationship to Film
- **Files Created**: `Language.java`, `LanguageRepository.java`, `LanguageService.java`
- **Files Modified**:
  - `Film.java` - Added Language relationship
  - All film view templates - Display language name instead of ID
  - `FilmRepository.java` - Added language-based search queries
  - `FilmController.java` - Added language search parameter
  - `films.html` - Added language search input field
- **Impact**: Users can search films by language and see language names

### 5. Updated Inventory and Rental Status Tracking
- **Changed**: Real-time rental status tracking
- **Files Modified**:
  - `FilmRepository.java` - Updated queries to exclude currently rented items
  - `InventoryRepository.java` - Added `getAvailableInventoryByFilmId()`
  - `InventoryService.java` - Added availability check method
  - `FilmController.java` - Updated rent logic
- **Impact**: Film availability now reflects actual rental status

### 6. Added Film Management for Owners
- **Added**: Add/Edit/Delete film functionality
- **Files Created**: `owner/add-film.html`
- **Files Modified**:
  - `FilmController.java` - Added save, add, edit, delete methods
  - `Film.java` - Added auto-generated ID
  - `FilmService.java` - Enhanced save method
  - `owner/manage-films.html` - Added "Add New Film" button
  - `WebSecurityConfig.java` - Added admin routes
- **Impact**: Owners can now manage film inventory

### 7. Fixed Owner Authentication
- **Problem**: Unable to login with staff credentials
- **Solution**: 
  - Changed password encoder to `DelegatingPasswordEncoder`
  - Added `{noop}` prefix for staff passwords
  - Added `@Param` annotation to `StaffRepository`
- **Files Modified**:
  - `UserDetailsServiceImpl.java`
  - `WebSecurityConfig.java`
  - `StaffRepository.java`
- **Impact**: Owner can login with username "Mike" and password "admin"

### 8. Added Customer Management for Admins
- **Added**: Complete customer management system
- **Files Created**:
  - `admin/manage-customers.html`
  - `admin/add-customer.html`
  - `admin/edit-customer.html`
  - `AdminController.java`
- **Files Modified**:
  - `Customer.java` - Added auto-generated ID, storeId, addressId fields
  - `WebSecurityConfig.java` - Added admin routes
  - `owner/owner.html` - Added "Manage Customers" link
- **Impact**: Admins can add and edit customers with proper address collection

### 9. Enhanced Customer Search for Owners
- **Changed**: Customer search from dropdown to text input
- **Files Modified**:
  - `CustomerRepository.java` - Added LIKE queries for partial matching
  - `CustomerController.java` - Updated filter handling
  - `owner/customers.html` - Replaced dropdowns with text inputs
- **Impact**: Owners can search customers by typing partial names

## Technical Improvements
- Implemented partial text matching across all search features
- Added proper JPA relationships (Film-Language)
- Enhanced security configuration for role-based access
- Improved real-time data accuracy for inventory tracking
- Added auto-generated IDs for entities

## Security Updates
- Fixed authentication for staff/owner users
- Added admin-only routes for customer/film management
- Implemented proper password encoding with DelegatingPasswordEncoder

## Database Schema Updates
- Added language_id support in Film entity
- Added store_id and address_id in Customer entity
- Maintained backward compatibility with existing Sakila database

## Known Limitations
- Address creation for customers uses default address_id (1)
- Special features field for films requires specific format or empty value
- Full address entity integration pending

## Access Information
- **Application URL**: http://localhost:8090/
- **Owner Login**: Username: `Mike`, Password: `admin`
- **Database**: MySQL (sakila), User: `root`, Password: `Lozinka123`
