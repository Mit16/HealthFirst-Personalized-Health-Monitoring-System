"use client";

import { Footer } from "flowbite-react";
import {
  BsDribbble,
  BsFacebook,
  BsGithub,
  BsInstagram,
  BsTwitter,
} from "react-icons/bs";

export function FooterComponent() {
  return (
    <Footer container className="bg-black text-white">
      <div className="w-full">
        <div className="grid w-full justify-between sm:flex sm:justify-between md:flex md:grid-cols-1">
          <div>
            {/* Replace Flowbite.Brand with your own logo or name */}
            <a href="/" className="flex items-center space-x-3 rtl:space-x-reverse">
              {/* <img
                src="/logo.svg"
                className="h-8"
                alt="HealthSync Logo"
              /> */}
              <span className="self-center text-2xl font-semibold whitespace-nowrap text-white">
                Healthfirst
              </span>
            </a>
          </div>
          <div className="grid grid-cols-2 gap-8 sm:mt-4 sm:grid-cols-3 sm:gap-6">
            <div>
              <h5 className="text-lg font-bold text-white">About</h5>
              <Footer.LinkGroup col>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  Our Project
                </Footer.Link>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  WearOS App
                </Footer.Link>
              </Footer.LinkGroup>
            </div>
            <div>
              <h5 className="text-lg font-bold text-white">Follow Us</h5>
              <Footer.LinkGroup col>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  GitHub
                </Footer.Link>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  Discord
                </Footer.Link>
              </Footer.LinkGroup>
            </div>
            <div>
              <h5 className="text-lg font-bold text-white">Legal</h5>
              <Footer.LinkGroup col>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  Privacy Policy
                </Footer.Link>
                <Footer.Link href="#" className="text-gray-300 hover:text-white">
                  Terms &amp; Conditions
                </Footer.Link>
              </Footer.LinkGroup>
            </div>
          </div>
        </div>
        <div className="w-full sm:flex sm:items-center sm:justify-between mt-6 border-t border-gray-700 pt-4">
          {/* Replaced Footer.Copyright */}
          <p className="text-sm text-gray-400 sm:text-center">
            © {new Date().getFullYear()} Healthfirst™. All Rights Reserved.
          </p>
          <div className="mt-4 flex space-x-6 sm:mt-0 sm:justify-center">
            <Footer.Icon href="#" icon={BsFacebook} className="text-white" />
            <Footer.Icon href="#" icon={BsInstagram} className="text-white" />
            <Footer.Icon href="#" icon={BsTwitter} className="text-white" />
            <Footer.Icon href="#" icon={BsGithub} className="text-white" />
            <Footer.Icon href="#" icon={BsDribbble} className="text-white" />
          </div>
        </div>
      </div>
    </Footer>
  );
}
