"use client";
import React, { useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";
import {
  Navbar,
  NavBody,
  NavItems,
  MobileNav,
  NavbarLogo,
  NavbarButton,
  MobileNavHeader,
  MobileNavToggle,
  MobileNavMenu,
} from "./ui/resizable-navbar";

const Header = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const { logout, token } = useContext(AuthContext);

  const handleLogout = () => {
    logout(); // ✅ context logout handles clearing + navigate
  };

  const navItems = [
    { name: "Dashboard", link: "/dashboard" },
    { name: "Submit Data", link: "/healthform" },
    { name: "Alerts", link: "/alerts" },
    { name: "Wear Integration", link: "/wear-integration" },
    { name: "Prediction", link: "/prediction" }, // ✅ Add this
  ];

  return (
    <div className="relative w-full z-10">
      <Navbar>
        {/* Desktop Navigation */}
        <NavBody>
          <Link
            to="/"
            className="relative z-20 mr-4 flex items-center space-x-2 px-2 py-1 text-sm font-normal text-black"
          >
            <img
              src="https://assets.aceternity.com/logo-dark.png"
              alt="logo"
              width={30}
              height={30}
            />
            <span className="font-medium text-black dark:text-white">
              Health Monitor
            </span>
          </Link>

          {token && (
            <>
              <NavItems items={navItems} onItemClick={() => {}} />
              <div className="flex items-center gap-4">
                <NavbarButton variant="dark" onClick={handleLogout}>
                  Logout
                </NavbarButton>
              </div>
            </>
          )}
        </NavBody>

        {/* Mobile Navigation */}
        {token && (
          <MobileNav>
            <MobileNavHeader>
              <Link
                to="/"
                className="relative z-20 flex items-center space-x-2 px-2 py-1 text-sm font-normal text-black"
              >
                <img
                  src="https://assets.aceternity.com/logo-dark.png"
                  alt="logo"
                  width={30}
                  height={30}
                />
                <span className="font-medium text-black dark:text-white">
                  Health Monitor
                </span>
              </Link>
              <MobileNavToggle
                isOpen={isMobileMenuOpen}
                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              />
            </MobileNavHeader>

            <MobileNavMenu
              isOpen={isMobileMenuOpen}
              onClose={() => setIsMobileMenuOpen(false)}
            >
              {navItems.map((item, idx) => (
                <Link
                  key={`mobile-link-${idx}`}
                  to={item.link}
                  onClick={() => setIsMobileMenuOpen(false)}
                  className="relative text-neutral-600 dark:text-neutral-300"
                >
                  <span className="block">{item.name}</span>
                </Link>
              ))}
              <NavbarButton
                onClick={() => {
                  handleLogout();
                  setIsMobileMenuOpen(false);
                }}
                variant="primary"
                className="w-full"
              >
                Logout
              </NavbarButton>
            </MobileNavMenu>
          </MobileNav>
        )}
      </Navbar>
    </div>
  );
};

export default Header;
